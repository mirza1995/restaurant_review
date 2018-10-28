package repositories;

import models.Cousine;
import models.Location;
import models.Restaurant;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class LocationRepository {
    @Inject private JPAApi api;

    public void createLocation(Location location){
        api.em().persist(location);
    }

    public void update(Location location){
        api.em().unwrap(Session.class).update(location);
    }

    public List<Location> getAllLocations(){
        return api.em().unwrap(Session.class).createCriteria(Location.class).list();
    }

    public Location get(int id){
        return (Location) api.em().unwrap(Session.class).createCriteria(Location.class).add(Restrictions.eq("id", id)).uniqueResult();
    }

    public boolean delete(int id){
        Location Location = (Location) api.em().unwrap(Session.class).createCriteria(Location.class).add(Restrictions.eq("id", id)).uniqueResult();
        api.em().unwrap(Session.class).delete(Location);
        return true;
    }

    public List<Location> getPopularLocations(){
        return  api.em().unwrap(Session.class).createCriteria(Location.class)
                .addOrder(Order.desc("numberOfRestaurants"))
                .setMaxResults(16)
                .list();
    }

    public Location getByName(String name){
        return (Location) api.em().unwrap(Session.class).createCriteria(Location.class).add(Restrictions.eq("locationName", name).ignoreCase()).uniqueResult();
    }

    public Number getNumberOfLocations(){
        return (Number) api.em().unwrap(Session.class).createCriteria(Location.class)
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    public List<Location> searchLocation(String search){
        return api.em().unwrap(Session.class).createCriteria(Location.class)
                .add( Restrictions.eq( "locationName", search ).ignoreCase()).list();
    }

    public List<Location> getLocationsPage(int page, int numberPerPage){
        return  api.em().unwrap(Session.class).createCriteria(Location.class)
                .setMaxResults(numberPerPage)
                .setFirstResult((page-1)*numberPerPage)
                .list();
    }
}