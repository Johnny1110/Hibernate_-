import entity.Event;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.Test;
import utility.JPAUtil;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class EnversTest {
    @Test
    public void testEvners(){
        // 持久化兩個 Event 實體
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist( new Event( "Our very first event!", new Date() ) );
        entityManager.persist( new Event( "A follow up event", new Date() ) );
        entityManager.getTransaction().commit();
        entityManager.close();

        // 使用 JPQL 查詢 Event 實體
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        List<Event> result = entityManager.createQuery( "from Event", Event.class ).getResultList();
        for ( Event event : result ) {
            System.out.println( "Event (" + event.getDate() + ") : " + event.getTitle() );
        }
        entityManager.getTransaction().commit();
        entityManager.close();

        // 以上是都跟之前一樣，以下是操作使用 Envers

        // 首先做一次修改
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        Event myEvent = entityManager.find( Event.class, 2L ); //
        myEvent.setDate( new Date() );
        myEvent.setTitle( myEvent.getTitle() + " (已修改過了喔!)" );
        entityManager.getTransaction().commit();
        entityManager.close();


        // 先看一下是否修改成功
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        myEvent = entityManager.find( Event.class, 2L );
        assertEquals( "A follow up event (已修改過了喔!)", myEvent.getTitle() );

        // 使用 AuditReader 查看實體被修改的歷史紀錄
        AuditReader reader = AuditReaderFactory.get(entityManager);
        Event firstRevision = reader.find( Event.class, 2L, 1 );
        assertFalse( firstRevision.getTitle().equals( myEvent.getTitle() ) );
        assertFalse( firstRevision.getDate().equals( myEvent.getDate() ) );
        Event secondRevision = reader.find( Event.class, 2L, 2 );
        assertTrue( secondRevision.getTitle().equals( myEvent.getTitle() ) );
        assertTrue( secondRevision.getDate().equals( myEvent.getDate() ) );
        entityManager.getTransaction().commit();
        entityManager.close();

        JPAUtil.closeEntityManagerFactory();
    }
}
