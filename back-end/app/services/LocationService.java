package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import helpers.Helper;
import models.Cousine;
import models.Location;
import org.postgresql.geometric.PGpath;
import org.postgresql.geometric.PGpoint;
import play.libs.Json;
import repositories.LocationRepository;
import repositories.PhotoRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class LocationService {
    @Inject private LocationRepository repository;

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public LocationService() {
        this.locationRepository = null;
    }

    public Location addLocation(JsonNode json){
        Location location = formLocationClass(json);
        repository.createLocation(location);
        return location;
    }

    public Location formLocationClass(JsonNode json){
        JsonNode rectanglePoints = json.get("rectanglePoints");
        PGpoint[] points = new PGpoint[5];
        for(int i = 0; i < rectanglePoints.size(); i++ ) {
            Double lat = Double.parseDouble(rectanglePoints.get(i).get("lat").toString());
            Double lng = Double.parseDouble(rectanglePoints.get(i).get("lng").toString());
            points[i] = new PGpoint(lat, lng);
        }
        Location location = new Location();
        location.setLocationName(json.get("locationName").asText());
        location.setNumberOfRestaurants(Integer.parseInt(json.get("numberOfRestaurants").asText()));
        location.setPoints(points);
        return location;
    }
    public Location updateLocation(JsonNode json){
        Location location = formLocationClass(json);
        location.setId(Integer.parseInt(json.get("id").toString()));
        repository.update(location);
        return location;
    }

    public List<Location> getPopularLocations(){
        return repository.getPopularLocations();
    }

    public Location getLocation(int id){
        if(id > 0){
            return repository.get(id);
        }
        return null;
    }

    public Location getLocationByName(String name){
        return repository.getByName(name);
    }


    public List<Location> getAllLocations(){
        return repository.getAllLocations();
    }
    
    public boolean deleteLocation(int id){
        return repository.delete(id);
    }

    public int getNumberOfLocations(){
        LocationRepository locationRepositoryUsed;
        if(this.locationRepository == null){
            locationRepositoryUsed = repository;
        } else {
            locationRepositoryUsed = locationRepository;
        }
        return locationRepositoryUsed.getNumberOfLocations().intValue();
    }

    public List<Location> getLocationsPage(int page){
        LocationRepository locationRepositoryUsed;
        if(this.locationRepository == null){
            locationRepositoryUsed = repository;
        } else {
            locationRepositoryUsed = locationRepository;
        }
        return locationRepositoryUsed.getLocationsPage(page,12);
    }

    public JsonNode getLocationsPage(String searchCriteria, int page){
        LocationRepository locationRepositoryUsed;
        if(this.locationRepository == null){
            locationRepositoryUsed = repository;
        } else {
            locationRepositoryUsed = locationRepository;
        }
        List<Location> locations;
        if(searchCriteria.equals("Empty")){
            return getLocationsWithNoSearch(page);
        } else {
            locations = locationRepositoryUsed.searchLocation(searchCriteria);
            JsonNode returnJson = makeLocationPage(page,12,locations);
            return returnJson;
        }
    }

    public JsonNode makeLocationPage(int page, int numberPerPage, List<Location> locations){
        JsonNode numberOfLocations = Json.parse("{\"numberOfLocations\":\""+ locations.size()+"\"}");
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode returnJson = objectMapper.createArrayNode();
        returnJson.add(numberOfLocations);

        if(locations.size()<numberPerPage){
            returnJson.add(Json.toJson(locations));
        } else {
            List<Location> locationsPage = new ArrayList<>();
            int border = (page - 1)*numberPerPage + numberPerPage;
            if(border > locations.size()){
                border = locations.size();
            }
            for(int i = (page-1)*numberPerPage; i < border; i++){
                locationsPage.add(locations.get(i));
            }
            returnJson.add(Json.toJson(locationsPage));
        }
        return returnJson;
    }

    public JsonNode getLocationsWithNoSearch (int page){
        LocationRepository locationRepositoryUsed;
        if(this.locationRepository == null){
            locationRepositoryUsed = repository;
        } else {
            locationRepositoryUsed = locationRepository;
        }
        List<Location> locations = getLocationsPage(page);
        JsonNode numberOfLocations = Json.parse("{\"numberOfLocations\":\""+locationRepositoryUsed.getNumberOfLocations()+"\"}");
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode returnJson = objectMapper.createArrayNode();
        returnJson.add(numberOfLocations);
        returnJson.add(Json.toJson(locations));
        return returnJson;
    }

    public JsonNode searchLocation(String searchCriteria){
        List<Location> locations = repository.searchLocation(searchCriteria);
        return Json.toJson(makeLocationPage(1,12,locations));
    }
}
