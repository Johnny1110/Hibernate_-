# 使用基本的 hbm 映射與原生 API

<br>

## 配置檔

### 配置什麼 ?

在使用 Hibernate 的專案中，程式當然不知道我們要使用 Hibernate，當然也不知道我們要連到那一個 database，使用怎樣的連接池，是否開啟快取等等。所以就需要先進行一個簡單的配置，最簡單的方法就是使用 hibernate.cfg.xml。

<br>

### hibernate.cfg.xml

* [看範例程式點這裡](../BasicHibernate/src/main/resources/hibernate.cfg.xml)

* 在 Resorces 資料夾中建立一個 xml 檔，全名叫做 hibernate.cfg.xml。

* 配置 SessionFactory，這是取的與資料庫連線最重要的一環，也是 Hibernate 原生 API 的重點對象。具體的配置都在 <property\> 標籤內設定。

    * 可以看到這裡先配置了資料庫連線設定，使用的是 in-memory 的 H2 資料庫 。( 資料存於 RAM 中 )

    * hbm2ddl.auto 設定為 create 模式可以更好的與 in-memory 資料庫做配合測試使用，詳見範例中註解。

    * connection pool 設定，使用的是 C3P0。詳見範例中註解。若不想使用第三方套件可以用 JDBC connection pool 取代 : 

            <property name="connection.pool_size">1</property>

    * SQL dialect 設定選定自己上面所配置的資料庫連線廠商，畢竟不同的資料庫 SQL 上都略有不同。

    * cache.provider_class 是設定二級快取的地方，這邊先設定關閉。

    * show_sql 設定為 true，可以在 console 中看到 Hibernate 自動生成的 SQL 指令。



    <br>
    <br>

## 實體

### 實體是什麼 ?

實體是資料庫在程式中的映射。簡單說就是用一個 Java 對象表達一個 Table 的結構。一個實體單位就是資料庫中的一筆資料。

<br>

### POJO -> Entity

* [看範例程式點這裡](../BasicHibernate/src/main/java/entity/Event.java)

* 建立一個類別叫做 Event.java 事實上就是一個簡單的 Java 對象 ( POJO )，但是待會經過配置之後，就會進化成實體 ( Entity )，進而可以被 Hibernate 管理。

* 特別要注意，實體中一定要有一預設個建構函式，提供 Hibernate 一個實例化實體的方法。


<br>
<br>

## 實體映射 hbm

* [看範例程式點這裡](../BasicHibernate/src/main/resources/Event.hbm.xml)

* 因為 Hibernate 並不知道我要將 Event.java 作為實體使用。所以我需要配置一個 Event.hbm.xml 來跟 Hibernate 交代清楚 Event.java 這個實體對應了資料庫的哪一個 Table 以及 具體欄位。

* <hibernate-mapping package="entity"\> 是指定實體所在的 package。

* <class name="Event" table="EVENTS"\> 這一段還蠻直觀的，類別名稱對應 table 名稱。要注意一下，這裡如果不指定 table="EVENTS" 則預設使用類別名稱當作 Table 名稱去對應。

* <id name="id" column="EVENT_ID"\>  id 會被 Hibernate 拿來當 PK 識別。使用名為 id 的類別成員，column="EVENT_ID" 不指定則預設使用 id 當作欄位名稱對應。

* <id\> 中主鍵的產生方式在這邊設定為 "increment"，表示主鍵的生成方式使用遞增數字決定。

* <property name="name" column="name"/\> 不多做解釋。

* 最後一定要記得在 hibernate.cfg.xml 的 SessionFactory 中注入已經配置好的 hbm 文件。

        <!-- 以下設置物件與資料庫表格映射文件 -->
        <mapping resource="Event.hbm.xml"/>


<br>
<br>

## 運行測試

### HibernateUtil

* [看範例程式點這裡](../BasicHibernate/src/main/java/utility/HibernateUtil.java)

* 在之前的 hibernate.cfg.xml 中我們已經配置好 SessionFactory 了，那他究竟是什麼呢 ?其實可以把他當作是 JDBC 中 Connection 的製造工廠，只不過他並不是製造 Connection，而是製造 Session。Session 是對 JDBC Connection 更高級的封裝，當只有在提交交易時才會取得 Connection 使用。

* 這裡就是簡單的初始化封裝 SessionFactory，以及生成 get 與 close 方法。


<br>

### Test

* [看範例程式點這裡](../BasicHibernate/src/test/java/BasicTest.java)

* 測試中使用 HibernateUtil 取得 SessionFactory，再通過 openSession( ) 方法取得 Session 物件。

* session.beginTransaction() 是打開交易管理機制，與 session.getTransaction().commit() 是一個閉合段落，在這個段落中的程式就是一筆交易，只有過程中所有程式碼正常執行才會最終把異動結果寫入資料庫，否則 rollback 回到交易前的狀態。

* 最終 session.close() 釋放系統資源。

* 第二段的查詢語法後面會講到。這邊先不急著了解。


