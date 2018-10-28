package repositories;
import models.Reservation;
import models.Restaurant;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class RestaurantCousineRepository {
    @Inject private JPAApi api;

    public Integer getNumberOfRestaurantsForCousine(int id){
        return  api.em().unwrap(Session.class).createSQLQuery ("SELECT DISTINCT restaurant_id FROM restaurant_cousines where cousine_id="+id)
                .list().size();
    }

}
