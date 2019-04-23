# 使用 Session

<br>

## Session 是什麼 ?


Hibernate 在對資料庫進行操作之前，必須先取得 Session 實例，相當於 JDBC 在對資料庫操作之前，必須先取得 Connection 實例， Session 是 Hibernate 原生 API 操作的基礎。

<br>
<br>

## 如何取得 Session ?

Session 的取得是由 SessionFactory 建立，而 SessionFactory 又是由讀取 hibernate.cfg.xml 中的配置信息生成。所以要了解如何取得 Session 就要先從如何取得 SessionFactory 說起。

### 取得 SessionFactory

* 使用 StandardServiceRegistry 註冊 hibernate.cfg.xml 中所配置的訊息 : 

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")
                        .build();

<br>

* 把 registry 注入給 MetadataSources 並建立 SessionFactory :

        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

<br>

### 取得 Session

* 使用 SessionFactory 取得 Session 實例。

        Session session = sessionFactory.openSession();

* 接下來就可以使用 session 中的各種方法來操作資料庫拉 !

        session.beginTransaction();
        session.save( new Event( "Our very first event!", new Date() ) );
        session.save( new Event( "A follow up event", new Date() ) );
        session.getTransaction().commit();
        session.close();


<br>
<br>

## 關於 Session

Session 並不等於 Connection。事實上 Session 實例在真正提交對資料庫操作時才會取用 Connection，如果設定了連接池就是直接取用池中的 Connection。使用結束再做歸還。

這邊要說明一下，並不是使用 Session 的 save( ) 或 update( ) 之類的方法後馬上就對資料庫進行異動，因為 Hibernate 的設計還有包括交易管理甚至設定資料異動量大於某數時才連線做異動，這都還是一門學問，等後面才會提到。 


<br>
<br>

## Session 基本操作

這裡的 Session 取得都是通過 HibernateUtil，其靜態載入並封裝了 SessionFactory。具體實作範例[點這裡](../BasicHibernate/src/main/java/utility/HibernateUtil.java)

<br>

### 儲存資料 ( 增 )

* 使用 save( ) 儲存資料。

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save( new Event( "Our very first event!", new Date() ) );
        session.save( new Event( "A follow up event", new Date() ) );
        session.getTransaction().commit();
        session.close();

  
* 在 session.beginTransaction( ) 與 session.getTransaction( ).commit( ) 之間叫做交易期間。

* save( ) 之後，不會馬上對資料庫進行更新，而是在session.getTransaction( ).commit( ) 之後才會對資料庫進行新增操作。

* 在交易期間的操作如果有一個錯誤丟出例外，則在資料庫層面會撤消所有更新操作，與 JDBC 中的 rollback( ) 一樣。

<br>

### 查詢資料 ( 查 )

* 使用 get( ) 或 load( ) :

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        //-- 取得 ID 為 1 的資料。 --//
        Event getEvent = (Event)session.get(Event.class, new Long(1)); 
        Event loadEvent = (Event)session.load(Event.class, new Long(1));
        session.getTransaction().commit();
        session.close();

  若找不到符合物件，get( ) 會回傳 null，load( ) 會拋出例外 : ObjectNotFoundException。

<br>
<br>

### 修改資料 ( 改 )

* 在 Session 中直接修改 :

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Event event = (Event)session.load(Event.class, new Long(1)); 
        event.setTitle("I am the latest Title !"); // 直接 set 值。
        session.getTransaction().commit();
        session.close();

  在 Session 中 load( ) 出來的實體就是持久化物件，簡單理解他就是資料庫的中的一欄。當然，這只是方便理解，事實上並非如此，具體到後面的 Persistence context 中詳細說明。

  <br>


* 如果在一個 Session load( ) 出的資料，要在另一個 Session 中更新，那就需要使用 update( ) 完成。 

        Session session1 = HibernateUtil.getSessionFactory().openSession();
        session1.beginTransaction();
        Event event = (Event)session1.load(Event.class, new Long(1));
        event.getTitle(); // 防止懶加載。
        session1.getTransaction().commit();
        session1.close();

        event.setTitle("I am the latest Title !");

        Session session2 = HibernateUtil.getSessionFactory().openSession();
        session2.beginTransaction();
        session2.update(event);
        session2.getTransaction().commit();
        session2.close();

  這邊要注意一下! 如果不在第一段中加入 event.getTitle( )，會報錯 : 

        could not initialize proxy - no Session

  因為 Hibernate 預設查詢都是使用懶加載模式，什麼是懶加載呢 ? 如果今天是 Event 有父關連聯表( User )關係如下 :

        public class Event {
        private Long id;
        private String title;
        private Date date;

        private User user; // 父表為 User

        // getter an setter
        }
  
  當我只是想查詢 Event 而已，那勢必會把不必要的 User 關聯表資料一起帶出來，這樣就造成不必要的資源浪費，Hibernate 當然也有方法預防，而方法就是使用懶加載模式。查詢時所回傳的實體都是代理實體 ( 空有其表 )，實體裡面是沒有資料的，只有當使用 get方法時才會真正去資料庫抓資料。懶加載是可以關閉的，但是不建議，畢竟是一個很好的效能優化方法。

  所以在此例中需要使用 Event 中隨便一個 get 方法激活查詢。至於為什麼使用不同 Session 的實體做更新要用 update( ) 方法，就要牽扯到 Entity 生命週期了，下一章節會提到。

  <br>

### 刪除資料 ( 刪 )

* 使用 delete( ) 刪除資料。

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Event event = (Event)session1.load(Event.class, new Long(1));
        session.delete(event);
        session.getTransaction().commit();
        session.close();


