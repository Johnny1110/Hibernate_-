# 使用 Maven 搭建專案

<br>

接下來就是實作的部分拉 ! 部屬方式有很多種，可以去官方網站上抓 Jar 檔，或者用 Gradle ... 部屬，這裡就不一一說明了道理都一樣，依賴都抓齊了就可以跑了 XD! 我習慣用 Maven 來做，這裡就看一下 pom 怎麼配置的吧。

<br>

## 在 pom.xml 配置

[看範例檔案點這裡](../pom.xml)


### Hibernate Core

* 這是 Hibernate 的核心模組，包含了 Hibernate 基本的依賴。

### Connection Pool

* 連接池的配置就比較自由了，Hibernate 並沒有特別指名要哪一個連接池，所以我就用學長推薦 C3P0。

### Second-Level Cache

* 二級緩存配置，關於緩存稍後會講到，Hibernate 也沒有特別指定，所以我就用官方推薦的 ehcache。

### Database Driver

* Database 本來 Hibernate 就來者不拒，為了方便做 in-memory 測試就使用 H2 資料庫。

### Logger

* Hibernate 必須使用日誌，所以就用 slf4j。

### Unit Test

* 單元測試，不多做解釋，用 JUnit。

<br>





