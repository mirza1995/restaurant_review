package repositories;

import models.Restaurant;
import models.RestaurantTable;
import models.Special;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TableRepository {
    @Inject private JPAApi api;

    public void createTable(RestaurantTable table){
        api.em().persist(table);
    }

    public void updateTable(RestaurantTable table){
        api.em().unwrap(Session.class).update(table);
    }

    public RestaurantTable getTable(int id){
        return (RestaurantTable) api.em().unwrap(Session.class).createCriteria(Special.class).add(Restrictions.eq("id", id)).uniqueResult();

    }
    public boolean deleteTable(int id){
        RestaurantTable table = (RestaurantTable) api.em().unwrap(Session.class).createCriteria(RestaurantTable.class).add(Restrictions.eq("id", id)).uniqueResult();
        api.em().unwrap(Session.class).delete(table);
        return true;
    }
    public boolean deleteRestaurantTables(int id){
        List<RestaurantTable> tables = api.em().unwrap(Session.class).createCriteria(RestaurantTable.class).add(Restrictions.eq("restaurant.id", id)).list();
        for(int i = 0; i < tables.size(); i++){
            api.em().unwrap(Session.class).delete(tables.get(i));
        }
        return true;
    }
    public List<RestaurantTable> getRestaurantTables(int restaurant_id){
        return api.em().unwrap(Session.class).createCriteria(RestaurantTable.class).add(Restrictions.eq("restaurant.id", restaurant_id)).list();
    }
}
