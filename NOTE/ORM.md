# ORM

<br>


## 什麼是 ORM ?

ORM ( Object Relational Mapping )，物件關係映射。它的實際作用簡單來說，就是把實際的關係型資料庫封裝成實體 (Entity) 也就是 Class。這樣一來就免於在程式碼中嵌入 SQL 來操作資料庫，而是改為直接對實體 (Entity) 進行操作。

<br>

## ORM 優缺點

### 優點

* 最大的優點對我來說就是免於再跟 SQL 打交道拉，直接對實體操作增刪改查就可以了。雖然 Hibernate 的研發工程師說 ORM 是給已經跟 SQL 打交道很久的人的一種獎勵工具。但是......

* 易理解的關聯式查詢。在 ORM 中針對 Table 之間的關聯可以用物件之間的依賴建構元表達。簡單的例子，父表 < T_USER > 子表 <T_Order>，父表中的 userId 欄位為子表的 FK，映射到實體上可如下表達 :

    #### T_USER

        public class User {
            Long userId;
            String name;
            String address;
            Set Order;

            //Getter and Setter ...
        }

    #### T_Order

        public class Order {
            Long orderId;
            boolean isPaid;
            User user;

            //Getter and Setter ...
        }

    可以看到 Order 實體中的 User 建構元就是其父表 T_USER 中的資料。User 實體中也有其子表 Order 的集合。這樣一來既實現一對多，多對一的關聯表達，也就是說在做單一表查詢的時候可以將關聯表中的資料一同帶出 ( 相當於 join )。往後會更詳細講解。


<br>

### 優點

* 早期有人說 ORM 性能差，像上面的第二個優點所展示的那樣，單表查詢同時帶出關聯表資料，可能會造成不必要的多餘查詢，這就是 ORM 的代價，但是就 Hibernate 而言，已經有很多相對應的機制了，像是 FetchType 設定 LAZY (覽加載，在真正取用關聯實體時才做查詢) 或者像是第一快取 ( Session 生命週期內 )、第二快取( SessionFactory 生命週期內 ) 都是解決效能問題的方案。



<br>
<br>

## ORM 框架

基於 Java 持久層的 ORM 框架有許多像是 MyBatis、TopLink、Spring Data。而這裡主要以 Hibernate 為主。接下來進入正題。


