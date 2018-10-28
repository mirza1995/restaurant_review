package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ImageUploaderService;
import javax.inject.Inject;

public class ImageUploaderController extends Controller {
    @Inject private ImageUploaderService service;

    @Transactional
    public Result uploadImage() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest();
        }
        String imageType = json.get("imageType").textValue();
        String base64String = json.get("photo").textValue();

        String outputFile = service.uploadImage(imageType,base64String);
        if(outputFile == null){
            return badRequest("Output file is null");
        } else if(outputFile == "Upload Failed"){
            notAcceptable("Problem with upload");
        } else if(outputFile == "Image null"){
            return badRequest("Image is not valid");
        } else if(outputFile == "Exception thrown"){
            return badRequest("Exception has been thrown");
        }
        return ok(Json.toJson(outputFile));
    }

    @Transactional
    public Result uploadGallery(){
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest();
        }
        service.uploadGallery(json);
        return ok("Photos Saved");
    }
}
