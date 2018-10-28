package services;

import com.fasterxml.jackson.databind.JsonNode;
import models.Restaurant;
import models.Review;
import models.User;
import repositories.ReviewRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ReviewService {
    @Inject private ReviewRepository repository;
    @Inject private RestaurantService restaurantService;
    @Inject private UserService userService;

    public Review addReview(Review review){
        repository.createReview(review);
        return review;
    }
    public Review updateReview(Review review){
        repository.update(review);
        return review;
    }

    public String reviewRestaurant(int userId, int restaurantId, String description, int rating){
        User user = userService.getUser(userId);
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);

        Review checkedReview=repository.getReview(user,restaurantId);

        boolean newReview = false;
        if(checkedReview == null){
            newReview = true;
            Review review = new Review();
            review.setUser(user);
            review.setRating(rating);
            review.setDescription(description);
            restaurant.increaseVotes();
            review.setRestaurant(restaurant);
            addReview(review);
        } else {
            checkedReview.setDescription(description);
            checkedReview.setRating(rating);
            updateReview(checkedReview);
        }
        List<Review> reviews = repository.getRestaurantReviews(restaurant.getId());
        double reviewRating = 0;
        for(int i = 0; i < reviews.size(); i++){
            reviewRating += reviews.get(i).getRating();
        }
        restaurant.setMark((int)Math.ceil(reviewRating/reviews.size()));
        restaurantService.updateRestaurant(restaurant);
        if(newReview){
            return "Review Added";
        }
        return "Review Updated";

    }
    
    public Review getReview(int id){
        return repository.get(id);
    }

    public boolean deleteReview(int id){
        return repository.delete(id);
    }
}
