package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.RestaurantTable;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.SpecialService;
import services.TableService;

import javax.inject.Inject;
public class TableController extends Controller  {
    @Inject private TableService tableService;

    @Transactional(readOnly = true)
    public Result getRestaurantTables(int id){
        if(id == 0){
            return ok("New Restaurant");
        }
        return ok(Json.toJson(tableService.getRestaurantTables(id)));
    }

    @Transactional
    public Result addTables(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        return ok(Json.toJson(tableService.addTables(json)));
    }

    @Transactional
    public Result deleteTable(int id){
        boolean deleted = tableService.deleteTable(id);
        if(deleted) {
            return ok("Table Deleted");
        }
        return badRequest();
    }
}
