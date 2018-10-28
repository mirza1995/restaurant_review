package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Meal;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.MealService;

import javax.inject.Inject;
import java.util.List;

public class MealController extends Controller {
    @Inject private MealService mealService;

    @Transactional(readOnly = true)
    public Result menu(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        List<Meal> menu = mealService.getMenu(Json.fromJson(json, Meal.class));
        if(menu == null){
            return notFound("Menu not found");
        }
        return ok(Json.toJson(menu));
    }

    @Transactional(readOnly = true)
    public Result getRestaurantMenu(int id){
        if(id == 0){
            return ok("New Restaurant");
        }
        return ok(Json.toJson(mealService.getRestaurantMenu(id)));
    }

    @Transactional
    public Result deleteMeal(int id){
        boolean deleted = mealService.deleteMeal(id);
        if(deleted) {
            return ok("Meal Deleted");
        }
        return badRequest();
    }

    @Transactional
    public Result addMenus(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        return ok(Json.toJson(mealService.addMenus(json)));
    }

}
