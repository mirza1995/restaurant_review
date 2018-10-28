package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ReviewService;
import javax.inject.Inject;

public class ReviewController extends Controller {
    @Inject private ReviewService reviewService;

    @Transactional
    public Result reviewRestaurant(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        String description = "";
        if(json.get("description") != null){
            description = json.get("description").asText();
        }
        int userId = Integer.parseInt(json.get("user_id").asText());
        int restaurantId = Integer.parseInt(json.get("restaurant_id").asText());
        int rating=Integer.parseInt(json.get("rating").asText());
        return ok(reviewService.reviewRestaurant(userId,restaurantId,description,rating));
    }
}
