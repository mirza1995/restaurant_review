package repositories;
import models.Special;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class SpecialRepository {
    @Inject private JPAApi api;

    public void createSpecial(Special special){
        api.em().persist(special);
    }

    public void update(Special special){
        api.em().unwrap(Session.class).update(special);
    }

    public List<Special> getAllSpecials(){
        return api.em().unwrap(Session.class).createCriteria(Special.class).list();
    }
    public Special get(int id){
        return (Special) api.em().unwrap(Session.class).createCriteria(Special.class).add(Restrictions.eq("id", id)).uniqueResult();

    }
    public boolean delete(int id){
        Special Special = (Special) api.em().unwrap(Session.class).createCriteria(Special.class).add(Restrictions.eq("id", id)).uniqueResult();
        api.em().unwrap(Session.class).delete(Special);
        return true;
    }
}
