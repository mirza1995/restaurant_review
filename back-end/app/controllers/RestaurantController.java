package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.*;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class RestaurantController extends Controller {
    @Inject private RestaurantService restaurantService;

    @Transactional(readOnly = true)
    public Result getRestaurantDetails(int id){
        Restaurant restaurant = restaurantService.getRestaurant(id);
        if(restaurant != null){
            return ok(Json.toJson(restaurant));
        }
        return ok("New Restaurant");
    }

    @Transactional(readOnly = true)
    public Result getAllRestaurants(){
        return ok(Json.toJson(restaurantService.getAllRestaurants()));
    }

    @Transactional(readOnly = true)
    public Result getPopularRestaurants(){
        return ok(Json.toJson(restaurantService.getPopularRestaurants()));
    }

    @Transactional(readOnly = true)
    public Result getNumberOfRestaurants(){
        return ok(Json.toJson(restaurantService.getNumberOfRestaurants()));
    }

    //Cousines
    @Transactional(readOnly = true)
    public Result getCousines(){
        return ok(restaurantService.getCousines());
    }

    //Search
    @Transactional(readOnly = true)
    public Result filterRestaurants(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        int page = Integer.parseInt(json.get("page").toString());
        String cousinesString = json.get("cousines").textValue();
        JsonNode restaurantCriterium = json.get("restaurantCriterium");
        Restaurant restaurantToSearch = Json.fromJson(restaurantCriterium,Restaurant.class);
        JsonNode restaurantFound = restaurantService.search(cousinesString, page, restaurantToSearch);
        if(restaurantFound == null){
            return notFound("Restaurant not found");
        }
        return ok(restaurantFound);
    }

    @Transactional(readOnly = true)
    public Result getRestaurantsPage(Integer page){
        return ok(Json.toJson(restaurantService.getRestaurantsPage(page,6)));
    }


    @Transactional(readOnly = true)
    public Result findTables(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        String date=json.get("date").textValue();
        String searchName = json.get("searchName").textValue().toLowerCase();
        String hours = json.get("hours").textValue();
        int persons = Integer.parseInt(json.get("persons").textValue());

        JsonNode restaurantFound = restaurantService.findTables(date,searchName,hours,persons);
        if(restaurantFound == null){
            return notFound("Restaurant not found");
        }
        return ok(restaurantFound);
    }

    @Transactional(readOnly = true)
    public Result findTable(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        String date=json.get("date").textValue();
        String searchName = json.get("searchName").textValue().toLowerCase();
        String hours = json.get("hours").textValue();
        int id= Integer.parseInt(json.get("id").textValue());
        int persons = Integer.parseInt(json.get("persons").asText());
        return ok(restaurantService.findTable(date,searchName,hours,id,persons));
    }

    @Transactional(readOnly = true)
    public Result getAllCousines(){
        return ok(restaurantService.getCousinesWithNoSearch(1));
    }

    @Transactional(readOnly = true)
    public Result getCousinesPage(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        int page = Integer.parseInt(json.get("page").toString());
        String searchCriteria = json.get("search").textValue();
        return ok(restaurantService.getCousinesPage(searchCriteria,page));
    }

    @Transactional(readOnly=true)
    public Result searchCousine(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        String searchCriteria = json.get("search").textValue();
        return ok(restaurantService.searchCousine(searchCriteria));
    }

    @Transactional(readOnly = true)
    public Result getRestaurantsForAdmin(){
        return ok(restaurantService.getRestaurantsWithNoSearch(1));
    }

    @Transactional(readOnly = true)
    public Result getRestaurantsPageAdmin(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        int page = Integer.parseInt(json.get("page").toString());
        String searchCriteria = json.get("search").textValue();
        return ok(restaurantService.getRestaurantsPageAdmin(searchCriteria,page));
    }

    @Transactional(readOnly=true)
    public Result searchRestaurantAdmin(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        String searchCriteria = json.get("search").textValue();
        return ok(restaurantService.searchRestaurantAdmin(searchCriteria));
    }

    @Transactional(readOnly=true)
    public Result getCousine(int id){
        Cousine cousine = restaurantService.getCousine(id);
        if(cousine != null){
            return ok(Json.toJson(cousine));
        }
        return ok("New Cousine");
    }

    @Transactional
    public Result deleteCousine(int id){
        boolean deleted = restaurantService.deleteCousine(id);
        if(deleted){
            return ok(Json.toJson(restaurantService.getCousinesWithNoSearch(1)));
        }
        return badRequest("Location Not Deleted");
    }

    @Transactional
    public Result updateCousine(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        Cousine cousine = Json.fromJson(json, Cousine.class);
        String cousineUpdated = restaurantService.updateCousine(cousine);
        if(cousineUpdated.equals("Invalid")){
            return badRequest("Invalid");
        }
        return created(Json.toJson(cousineUpdated));
    }

    @Transactional
    public Result createCousine(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        Cousine cousine = Json.fromJson(json, Cousine.class);
        String cousineCreated = restaurantService.createCousine(cousine);
        if(cousineCreated.equals("Invalid")){
            return badRequest("Invalid");
        }
        return created(Json.toJson(cousineCreated));
    }


    @Transactional
    public Result addRestaurant(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        return created(Json.toJson(restaurantService.addRestaurant(json)));
    }

    @Transactional
    public Result updateRestaurant(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        return created(Json.toJson(restaurantService.updateRestaurant(json)));
    }

    @Transactional
    public Result deleteRestaurant(int id){
        boolean deleted = restaurantService.deleteRestaurant(id);
        if(deleted) {
            return ok(Json.toJson(restaurantService.getRestaurantsWithNoSearch(1)));
        }
        return badRequest();
    }

    @Transactional
    public Result closestRestaurants(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        double latitude = json.get("latitude").asDouble();
        double longitude = json.get("longitude").asDouble();
        return ok(Json.toJson(restaurantService.closestRestaurants(latitude,longitude)));
    }

    @Transactional
    public Result getClosestRestaurantsForMap(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        double latitude = json.get("latitude").asDouble();
        double longitude = json.get("longitude").asDouble();
        return ok(Json.toJson(restaurantService.getClosestRestaurantsForMap(latitude,longitude)));
    }


    @Transactional
    public Result getCousinesForCarousel(){
        return ok(restaurantService.getCousinesForCarousel());
    }
}
