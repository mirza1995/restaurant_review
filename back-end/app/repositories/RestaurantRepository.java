package repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Cousine;
import models.Location;
import models.Restaurant;
import models.RestaurantTable;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.type.StandardBasicTypes;
import play.db.jpa.JPA;
import play.db.jpa.JPAApi;
import play.libs.Json;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;


@Singleton
public class RestaurantRepository{
    @Inject private JPAApi api;

    public void createRestaurant(Restaurant restaurant){
        api.em().persist(restaurant);
    }

    public void updateRestaurant(Restaurant restaurant){
        api.em().unwrap(Session.class).update(restaurant);
    }


    public List<Restaurant> getAllRestaurants(){
        return api.em().unwrap(Session.class).createCriteria(Restaurant.class).list();
    }

    public Restaurant get(int id){
        return (Restaurant) api.em().unwrap(Session.class).createCriteria(Restaurant.class).add(Restrictions.eq("id", id)).uniqueResult();
    }

    public boolean delete(int id){
        Restaurant Restaurant = (Restaurant) api.em().unwrap(Session.class).createCriteria(Restaurant.class).add(Restrictions.eq("id", id)).uniqueResult();
        api.em().unwrap(Session.class).delete(Restaurant);
        return true;
    }

    public List<Restaurant> getRestaurantsPage(int page, int numberPerPage){
        return  api.em().unwrap(Session.class).createCriteria(Restaurant.class)
                .setFirstResult((page-1)*numberPerPage)
                .setMaxResults(numberPerPage)
                .list();
    }

    public List<Restaurant> searchRestaurant(String search, Restaurant restaurant, int page, int numberPerPage){
        if(restaurant.getMark() == 0 && restaurant.getPriceRange() == 0 && restaurant.getRestaurantName() == ""){
            return getAllRestaurants();
        } else {
            Criteria criterium=api.em().unwrap(Session.class).createCriteria(Restaurant.class);
            if(restaurant.getMark() > 0){
                criterium.add(Restrictions.eq("mark", restaurant.getMark()));
            }
            if(restaurant.getPriceRange() > 0){
                criterium.add(Restrictions.eq("priceRange", restaurant.getPriceRange()));
            }
            if(restaurant.getRestaurantName() != null && restaurant.getRestaurantName() != ""){
                criterium.add(Restrictions.eq("restaurantName", restaurant.getRestaurantName()).ignoreCase());
            }
            return criterium.list();
        }
    }

    public List<Restaurant> filterRestaurants(String search, Location location){
        if(search == ""){
            return getAllRestaurants();
        } else if(location == null){
            return api.em().unwrap(Session.class).createCriteria(Restaurant.class)
                    .add(Restrictions.eq("restaurantName", search).ignoreCase()).list();
        } else {
            return api.em().unwrap(Session.class).createCriteria(Restaurant.class)
                    .add(Restrictions.or(
                            Restrictions.eq("restaurantName", search).ignoreCase(),
                            Restrictions.eq("location.id", location.getId())
                    )).list();
        }
    }

    public Number getNumberOfRestaurants(){
        return (Number) api.em().unwrap(Session.class).createCriteria(Restaurant.class)
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    public Number getNumberOfCousines(){
        return (Number) api.em().unwrap(Session.class).createCriteria(Cousine.class)
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    public List<Cousine> searchCousine(String search){
        return api.em().unwrap(Session.class).createCriteria(Cousine.class)
                .add( Restrictions.eq( "name", search ).ignoreCase()).list();
    }

    public List<Cousine> getCousinesPage(int page, int numberPerPage){
        return  api.em().unwrap(Session.class).createCriteria(Cousine.class)
                .setMaxResults(numberPerPage)
                .setFirstResult((page-1)*numberPerPage)
                .list();
    }

    public List<Restaurant> searchRestaurantAdmin(String search){
        return api.em().unwrap(Session.class).createCriteria(Restaurant.class)
                .add( Restrictions.eq( "restaurantName", search ).ignoreCase()).list();
    }

    public Cousine getCousine(int id){
        return (Cousine) api.em().unwrap(Session.class).createCriteria(Cousine.class).add(Restrictions.eq("id", id)).uniqueResult();
    }

    public boolean deleteCousine(int id){
        Cousine cousine = (Cousine) api.em().unwrap(Session.class).createCriteria(Cousine.class).add(Restrictions.eq("id", id)).uniqueResult();
        api.em().unwrap(Session.class).delete(cousine);
        return true;
    }

    public void createCousine(Cousine cousine){
        api.em().persist(cousine);
    }

    public void updateCousine(Cousine cousine){
        api.em().unwrap(Session.class).update(cousine);
    }

    public List<Restaurant> getClosestRestaurants(double latitude,double longitude){
        return api.em().unwrap(Session.class).createSQLQuery ("SELECT * from restaurants ORDER BY ST_Distance(\n" +
                "\t\tST_Transform(ST_SetSRID(ST_MakePoint("+longitude+","+latitude+"),4326),2100),\n" +
                "\t\tST_Transform(ST_SetSRID(ST_MakePoint(longitude,latitude),4326),2100)\n" +
                "\t) ASC").addEntity(Restaurant.class)
                .list();
    }

}
