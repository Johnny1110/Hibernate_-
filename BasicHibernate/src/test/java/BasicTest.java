import entity.Event;
import org.hibernate.Session;
import org.junit.Test;
import utility.HibernateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

    @Test
    public void killing(){
        // 填入試驗值。
        List<Integer> group = new ArrayList<Integer>();
        for(int i = 0 ; i < 1000 ; i++){
            group.add( i + 1);
        }
        group = filterValue(group);
        group.forEach(g ->{
            System.out.print(g + " ");
        });
    }

    public static List<Integer> filterValue(List<Integer> input){
        int listSize = input.size();
        List<Integer> newGroup = new ArrayList<Integer>();
        for(int i = 0 ; i < listSize ; i ++){
            if((i + 1) % 2 == 0){
                newGroup.add(input.get(i));
            }
        }
        if(newGroup.size() > 1){
            newGroup = filterValue(newGroup);
        }
        return newGroup;
    }
}
