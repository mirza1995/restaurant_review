package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.*;

import javax.inject.Inject;
import java.util.List;

public class UserController  extends Controller {
    @Inject private UserService userService;

    @Transactional
    public Result createUser(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        User user = Json.fromJson(json, User.class);
        String userCreated = userService.createUser(user);

        if(userCreated.equals("Invalid")){
            return badRequest("Invalid");
        }
        if(userCreated.equals("Exists")){
            return notAcceptable("Exists");
        }
        session("loggedIn", userCreated);
        return created(Json.toJson(userService.getUser(Integer.parseInt(userCreated))));
    }

    @Transactional
    public Result addUserWithoutLogin(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        User user = Json.fromJson(json, User.class);
        String userCreated = userService.createUser(user);

        if(userCreated.equals("Invalid")){
            return badRequest("Invalid");
        }
        if(userCreated.equals("Exists")) {
            return notAcceptable("Exists");
        }
        return created(Json.toJson(userService.getUser(Integer.parseInt(userCreated))));
    }



    @Transactional
    public Result login(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest("Invalid arguments in login information");
        }
        User user = userService.login(Json.fromJson(json, User.class));
        if(user==null){
            return badRequest("User not found");
        }
        JsonNode jsonObject = Json.toJson(user);
        session("loggedIn", Integer.toString(user.getId()));
        return ok(jsonObject);
    }

    public Result logout() {
        session().clear();
        return ok("Logged Out");
    }

    @Transactional
    public Result getCurrentUser(){
        String sessionId = session("loggedIn");
        if(sessionId != null) {
            return ok(userService.getCurrentUser(Integer.parseInt(sessionId)));
        } else {
            return ok("Not Logged In");
        }
    }

    @Transactional(readOnly = true)
    public Result getAllUsers(){
        return ok(Json.toJson(userService.getUsersPageWithNoSearch(1)));
    }

    @Transactional(readOnly=true)
    public Result getNumberOfUsers(){
        return ok(Json.toJson(userService.getNumberOfUsers()));
    }

    @Transactional(readOnly=true)
    public Result getUser(int id){
        User user = userService.getUser(id);
        if(user != null){
            return ok(Json.toJson(user));
        }
        return ok("New User");
    }

    @Transactional
    public Result deleteUser(int id){
        boolean deleted = userService.deleteUser(id);
        if(deleted){
            return ok(Json.toJson(userService.getUsersPageWithNoSearch(1)));
        }
        return badRequest("User Not Deleted");
    }

    @Transactional
    public Result updateUser(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        User user = Json.fromJson(json, User.class);
        String userCreated = userService.updateUser(user);

        if(userCreated.equals("Invalid")){
            return badRequest("Invalid");
        }
        session("loggedIn", userCreated);
        return created(Json.toJson(userCreated));
    }

    @Transactional
    public Result updateUserWithoutLogin(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        User user = Json.fromJson(json, User.class);
        String userCreated = userService.updateUser(user);

        if(userCreated.equals("Invalid")){
            return badRequest("Invalid");
        }
        return created(Json.toJson(userCreated));
    }

    @Transactional(readOnly=true)
    public Result searchUser(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        String searchCriteria = json.get("search").textValue();
        return ok(userService.searchUser(searchCriteria));
    }

    @Transactional(readOnly=true)
    public Result getUsersPage(){
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest();
        }
        int page = Integer.parseInt(json.get("page").toString());
        String searchCriteria = json.get("search").textValue();
        return ok(userService.getUsersPage(searchCriteria,page));
    }


}
