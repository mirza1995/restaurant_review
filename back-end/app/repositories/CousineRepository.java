package repositories;

import models.Cousine;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class CousineRepository {
    @Inject private JPAApi api;

    public void createCousine(Cousine cousine){
        api.em().persist(cousine);
    }

    public void update(Cousine cousine){
        api.em().unwrap(Session.class).update(cousine);
    }

    public List<Cousine> getAllCousines(){
        return api.em().unwrap(Session.class).createCriteria(Cousine.class).list();
    }

    public Cousine getCousineByName(String name){
        return (Cousine) api.em().unwrap(Session.class).createCriteria(Cousine.class).add(Restrictions.eq("name", name)).uniqueResult();
    }

    public boolean delete(int id){
        Cousine Cousine = (Cousine) api.em().unwrap(Session.class).createCriteria(Cousine.class).add(Restrictions.eq("id", id)).uniqueResult();
        api.em().unwrap(Session.class).delete(Cousine);
        return true;
    }
}
