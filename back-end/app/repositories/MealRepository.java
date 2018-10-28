package repositories;

import models.Meal;
import models.Restaurant;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class MealRepository {
    @Inject
    private JPAApi api;

    public void createMeal(Meal meal){
        api.em().persist(meal);
    }

    public void update(Meal meal){
        api.em().unwrap(Session.class).update(meal);
    }

    public List<Meal> getMeals(Meal meal){
        return api.em().unwrap(Session.class).createCriteria(Meal.class).add(Restrictions.eq("restaurant.id", meal.getId())).add(Restrictions.eq("type", meal.getType())).list();
    }

    public Meal get(int id){
        return (Meal) api.em().unwrap(Session.class).createCriteria(Meal.class).add(Restrictions.eq("id", id)).uniqueResult();
    }

    public List<Meal> getRestaurantMeals(int id){
        return api.em().unwrap(Session.class).createCriteria(Meal.class).add(Restrictions.eq("restaurant.id", id)).list();
    }
    public boolean delete(int id){
        Meal Meal = (Meal) api.em().unwrap(Session.class).createCriteria(Meal.class).add(Restrictions.eq("id", id)).uniqueResult();
        api.em().unwrap(Session.class).delete(Meal);
        return true;
    }

    public List<Meal> getRestaurantMenu(int id){
        return api.em().unwrap(Session.class).createCriteria(Meal.class).add(Restrictions.eq("restaurant.id", id)).list();
    }
}
