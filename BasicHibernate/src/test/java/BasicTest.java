import entity.Event;
import org.hibernate.Session;
import org.junit.Test;
import utility.HibernateUtil;

import java.util.Date;
import java.util.List;

public class BasicTest {

    @Test
    public void testBasicUsage() {
        // 持久化 2 個 Event 實體
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save( new Event( "Our very first event!", new Date() ) );
        session.save( new Event( "A follow up event", new Date() ) );
        session.getTransaction().commit();
        session.close();

        // 使用 HQL 查詢所有 Event 資料
        Session session1 = HibernateUtil.getSessionFactory().openSession();
        session1.beginTransaction();
        List result = session1.createQuery( "from Event " ).list();
        for ( Event event : (List<Event>) result ) {
            System.out.println( "Event (" + event.getDate() + ") : " + event.getTitle() );
        }
        session1.getTransaction().commit();
        session1.close();
        HibernateUtil.closeSessionFactory();
    }
}
