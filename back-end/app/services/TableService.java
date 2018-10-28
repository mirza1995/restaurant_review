package services;


import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.libs.Json;
import repositories.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class TableService {
    @Inject private TableRepository repository;
    @Inject private RestaurantRepository restaurantRepository;

    public RestaurantTable addTable(RestaurantTable table){
        repository.createTable(table);
        return table;
    }

    public RestaurantTable updateRestaurantTable(RestaurantTable table){
        repository.updateTable(table);
        return table;
    }

    public RestaurantTable getTable(int id){
        return repository.getTable(id);
    }

    public List<RestaurantTable> getRestaurantTables(int restaurant_id){
        return repository.getRestaurantTables(restaurant_id);
    }

    public boolean deleteTable(int id){
        return repository.deleteTable(id);
    }

    public List<RestaurantTable> addTables(JsonNode jsonTables){
        List<RestaurantTable> tables = new ArrayList<>();
        JsonNode tablesJson = jsonTables.get("objects");
        for(int i = 0; i < tablesJson.size(); i++){
            JsonNode json = tablesJson.get(i);
            RestaurantTable table = Json.fromJson(json, RestaurantTable.class);
            if(table.getId() != null){
                repository.updateTable(table);
                tables.add(table);
            } else {
                repository.createTable(table);
                tables.add(table);
            }
        }
        return tables;
    }
}
