package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.PhotoService;

import javax.inject.Inject;
import java.util.ArrayList;

public class PhotoController extends Controller {
    @Inject private PhotoService photoService;

    @Transactional(readOnly = true)
    public Result getRestaurantPhotos(int id){
        if(id == 0){
            return ok(Json.toJson(new ArrayList<>()));
        }
        JsonNode photos=photoService.getRestaurantPhotos(id,0);
        if(photos==null){
            return notFound("No photos");
        }
        return ok(photos);
    }
    @Transactional(readOnly = true)
    public Result getGallery(int id){
        JsonNode photos=photoService.getRestaurantPhotos(id,7);
        if(photos==null){
            return notFound("No photos");
        }
        return ok(photos);
    }
    @Transactional(readOnly = true)
    public Result getNumberOfPhotos(int id){
        return ok(Json.toJson(photoService.getAllPhotos(id).size()));
    }
}
