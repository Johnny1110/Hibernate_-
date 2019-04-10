import entity.Event;
import org.hibernate.Session;
import org.junit.Test;
import utility.HibernateUtil;

import java.util.Date;
import java.util.List;

public class AnnoationsTest {
    @Test
    public void testAnnoations(){
        // 實例化 Event ...
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save( new Event( "Our very first event!", new Date() ) );
        session.save( new Event( "A follow up event", new Date() ) );
        session.getTransaction().commit();
        session.close();

        // 用 HQL 查詢
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List result = session.createQuery( "from Event" ).list();
        for ( Event event : (List<Event>) result ) {
            System.out.println( "Event (" + event.getDate() + ") : " + event.getTitle() );
        }
        session.getTransaction().commit();
        session.close();
        HibernateUtil.closeSessionFactory();
    }

}
