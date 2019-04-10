package entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;

@Entity // 等同於映射文件中的<class>
@Table( name = "EVENTS" ) //如果不指定 table name = EVENTS，那預設為 EVENT(通過反射機制得到 class 名稱)
@Audited  // @Audited 啟動 Envers 來追蹤這個實體的變化
public class Event {
    private Long id;
    private String title;
    private Date date;

    public Event() { }

    public Event(String title, Date date) {
        this.title = title;
        this.date = date;
    }

    @Id // 定義實體標識符的屬性
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment") // @GeneratedValue 與 @GenericGenerator 把 increment 生成策略用於此實體的標識符值
    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP) // date 屬性需要特殊處理，指定 type
    @Column(name = "EVENT_DATE") // "DATE" 是 SQL 保留字，所以要指定義欄位名稱
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "TITLE") // 可有可無，因為反射機制會自動取得 get後面的字串轉大寫作欄位名稱使用
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
