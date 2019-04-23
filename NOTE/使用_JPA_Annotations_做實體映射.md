# 使用 JPA Annotations 做實體映射

<br>

## 將 Entity 與 hbm.xml 結合

由於我一開始接觸 Hibernate 的時候，就是使用 JPA 註釋來代替 hbm.xml，直接在 POJO 做映射設定。那個時候一直不理解為什麼要有 hbm 的存在，明明有這麼好的方式來做實體映射，誰會想用 hbm ? 事實上真的是這樣，爬過文之後發現除了老案子 ( Hibernate 3.2 以前 )，不然都是使用 JPA 註釋來做映射。

這裡重新做一次上個章節的映射設定，不過使用的是 JPA 註釋。

<br>

## 實體設定

以下就是一個使用 JPA 註釋完成映射的例子，完成對其設定之後，這個 Class 不需要 hbm 就可以獨立作為 Entity 運作。

* [看範例程式點這裡](../AnnotationsMapping/src/main/java/entity/Event.java)

* 注意 : 註釋都來源於 javax.persistence 這個套件中。

* @Entity 是類級別註釋，宣告這個 Class 是一個實體。

* @Table( name = "EVENTS" ) 是類級別註釋，宣告實體所映射的 Table。

* @Id 是方法級別註釋，定義 PK 欄位。 

* 最後也是最重要的一步，就是把這個實體告訴 hibernate.cfg.xml。在 session-factory 中如下設定 : 

        <!-- 設定實體路徑 (entity 項下的 Event 類別)-->
        <mapping class="entity.Event"/>

* 如此一來就可以使用 Session 對 Event 這個實體做操做了。