import entity.Event;
import org.junit.Test;
import utility.JPAUtil;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public class JPATest {
    @Test
    public void testJPA(){
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist( new Event( "Our very first event!", new Date() ) );
        entityManager.persist( new Event( "A follow up event", new Date() ) );
        entityManager.getTransaction().commit();
        entityManager.close();


        EntityManager entityManager1 = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager1.getTransaction().begin();
        List<Event> result = entityManager1.createQuery( "from Event", Event.class ).getResultList();
        for ( Event event : result ) {
            System.out.println( "Event (" + event.getDate() + ") : " + event.getTitle() );
        }
        entityManager1.getTransaction().commit();
        entityManager1.close();

        JPAUtil.closeEntityManagerFactory();
    }
}
