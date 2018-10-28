package services;


import com.fasterxml.jackson.databind.JsonNode;
import models.Meal;
import models.Restaurant;
import models.RestaurantTable;
import play.libs.Json;
import repositories.MealRepository;
import repositories.RestaurantRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MealService {
    @Inject private MealRepository repository;
    @Inject private RestaurantRepository restaurantRepository;

    public Meal addMeal(Meal meal){
        repository.createMeal(meal);
        return meal;
    }
    public Meal updateMeal(Meal meal){
        repository.update(meal);
        return meal;
    }

    public Meal getMeal(int id){
        return repository.get(id);
    }

    public boolean deleteMeal(int id){
        return repository.delete(id);
    }

    public List<Meal> getMenu(Meal meal){
        return repository.getMeals(meal);
    }

    public List<Meal> getRestaurantMenu(int id){
        return repository.getRestaurantMenu(id);
    }

    public List<Meal> addMenus(JsonNode jsonMenus){
        List<Meal> menus = new ArrayList<>();
        JsonNode menusJson = jsonMenus.get("objects");
        for(int i = 0; i < menusJson.size(); i++){
            JsonNode json = menusJson.get(i);
            Meal menu = Json.fromJson(json, Meal.class);
            if(menu.getId() != null){
                repository.update(menu);
                menus.add(menu);
            } else {
                repository.createMeal(menu);
                menus.add(menu);
            }
        }
        return menus;
    }
}
