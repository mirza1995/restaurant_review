package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import models.Location;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.*;

import javax.inject.Inject;

public class LocationController extends Controller {
    @Inject private LocationService locationService;

    @Transactional(readOnly = true)
    public Result getPopularLocations(){
        return ok(Json.toJson(locationService.getPopularLocations()));
    }

    @Transactional(readOnly=true)
    public Result getNumberOfLocations(){
        return ok(Json.toJson(locationService.getNumberOfLocations()));
    }

    @Transactional(readOnly = true)
    public Result getAllLocations(){
        return ok(locationService.getLocationsWithNoSearch(1));
    }

    @Transactional(readOnly = true)
    public Result getLocationsPage(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        int page = Integer.parseInt(json.get("page").toString());
        String searchCriteria = json.get("search").textValue();
        return ok(locationService.getLocationsPage(searchCriteria, page));
    }

    @Transactional(readOnly=true)
    public Result searchLocations(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        String searchCriteria = json.get("search").textValue();
        return ok(locationService.searchLocation(searchCriteria));
    }

    @Transactional(readOnly=true)
    public Result getLocation(int id){
        Location location = locationService.getLocation(id);
        if(location != null){
            return ok(Json.toJson(location));
        }
        return ok("New Location");
    }

    @Transactional
    public Result deleteLocation(int id){
        boolean deleted = locationService.deleteLocation(id);
        if(deleted){
            return ok(Json.toJson(locationService.getLocationsWithNoSearch(1)));
        }
        return badRequest("Location Not Deleted");
    }

    @Transactional
    public Result updateLocation(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        return created(Json.toJson(locationService.updateLocation(json)));
    }

    @Transactional
    public Result createLocation(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        /*Location location = locationservice.addlocation(json)
        if(location == null){
            return badrequest("invalid");
        }*/
        return created(Json.toJson(locationService.addLocation(json)));
    }

    @Transactional(readOnly=true)
    public Result getLocationByName(String name){
        Location location = locationService.getLocationByName(name);
        return ok(Json.toJson(location));
    }
}
