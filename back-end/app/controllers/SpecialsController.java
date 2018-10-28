package controllers;

import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.SpecialService;

import javax.inject.Inject;

public class SpecialsController extends Controller {
    @Inject private SpecialService specialService;

    @Transactional(readOnly = true)
    public Result getAllSpecials(){
        return ok(Json.toJson(specialService.getAllSpecials()));
    }
}
