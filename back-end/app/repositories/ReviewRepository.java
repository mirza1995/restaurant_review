package repositories;

import models.Review;
import models.Restaurant;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ReviewRepository {
    @Inject private JPAApi api;

    public void createReview(Review review){
        api.em().persist(review);
    }

    public void update(Review review){
        api.em().unwrap(Session.class).update(review);
    }

    public Review get(int id){
        return (Review) api.em().unwrap(Session.class).createCriteria(Review.class).add(Restrictions.eq("id", id)).uniqueResult();
    }

    public Review getReview(User user,int restaurantId){
        return (Review) api.em().unwrap(Session.class).createCriteria(Review.class).add(Restrictions.eq("user.id", user.getId())).add(Restrictions.eq("restaurant.id", restaurantId)).uniqueResult();
    }
    public boolean delete(int id){
        Review Review = (Review) api.em().unwrap(Session.class).createCriteria(Review.class).add(Restrictions.eq("id", id)).uniqueResult();
        api.em().unwrap(Session.class).delete(Review);
        return true;
    }

    public List<Review> getRestaurantReviews(int id){
        return api.em().unwrap(Session.class).createCriteria(Review.class).add(Restrictions.eq("restaurant.id", id)).list();
    }
}
