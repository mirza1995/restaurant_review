package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import helpers.Helper;
import models.Restaurant;
import models.User;
import play.libs.Json;
import repositories.PhotoRepository;
import repositories.UserRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import javax.xml.bind.*;
@Singleton
public class UserService {
    @Inject private UserRepository repository;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserService() {
        this.userRepository = null;
    }

    public User addUser(User user){
        String password = Helper.hashPassword(user);
        if(password == null){
            return null;
        }
        user.setPassword(password);
        User checkEmailUser=repository.checkEmail(user.getEmail());
        if(checkEmailUser != null){
            return null;
        }
        repository.createUser(user);
        return user;
    }

    public String createUser(User user){

        if(!Helper.isValidEmail(user.getEmail()) || user.getPassword().length()<4 || !user.getPhoneNumber().matches("[0-9]+") || user.getPhoneNumber().length() < 9 ){
            return "Invalid";
        }
        User userCreated = addUser(user);
        if(userCreated == null){
            return "Exists";
        }
        return Integer.toString(userCreated.getId());
    }
    public String updateUser(User user){
        if(!Helper.isValidEmail(user.getEmail()) || user.getPassword().length()<4 || !user.getPhoneNumber().matches("[0-9]+") || user.getPhoneNumber().length() < 9 ){
            return "Invalid";
        }
        String password = Helper.hashPassword(user);
        if(password == null){
            return null;
        }
        user.setPassword(password);
        repository.update(user);
        return String.valueOf(user.getId());
    }

    public User login(User userToLogin){
        User user = repository.login(userToLogin);
        return user;
    }

    public User getUser(int id){
        if(id > 0){
            return repository.getUser(id);
        }
        return null;
    }

    public JsonNode getCurrentUser(int id){
        User user = repository.getUser(id);
        return Json.parse("{\"id\":\""+id+"\",\"userName\":\""+user.getFirstName()+"\",\"account\":\""+user.getAccount()+"\"}");
    }

    public List<User> getAllUsers(){
        return repository.getAllUsers();
    }

    public boolean deleteUser(int id){
        return repository.deleteUser(id);
    }

    public int getNumberOfUsers(){
        UserRepository userRepositoryUsed;
        if(this.userRepository == null){
            userRepositoryUsed = repository;
        } else {
            userRepositoryUsed = userRepository;
        }
        return userRepositoryUsed.getNumberOfUsers().intValue();
    }

    public JsonNode searchUser(String searchCriteria){
        List<User> users = repository.searchUser(searchCriteria);
        return Json.toJson(makeUsersPage(1,6,users));
    }

    public List<User> getUsersPage(int page){
        UserRepository userRepositoryUsed;
        if(this.userRepository == null){
            userRepositoryUsed = repository;
        } else {
            userRepositoryUsed = userRepository;
        }
        return userRepositoryUsed.getUsersPage(page,6);
    }

    public JsonNode getUsersPageWithNoSearch (int page){
        List<User> users = getUsersPage(page);
        JsonNode numberOfUsers = Json.parse("{\"numberOfUsers\":\""+getNumberOfUsers()+"\"}");
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode returnJson = objectMapper.createArrayNode();
        returnJson.add(numberOfUsers);
        returnJson.add(Json.toJson(users));
        return returnJson;
    }

    public JsonNode getUsersPage(String searchCriteria, int page){
        UserRepository userRepositoryUsed;
        if(this.userRepository == null){
            userRepositoryUsed = repository;
        } else {
            userRepositoryUsed = userRepository;
        }
        List<User> users;
        if(searchCriteria.equals("Empty")){
            return getUsersPageWithNoSearch(page);
        } else {
            users = userRepositoryUsed.searchUser(searchCriteria);
            JsonNode returnJson = makeUsersPage(page,6,users);
            return returnJson;
        }
    }

    public JsonNode makeUsersPage(int page, int numberPerPage, List<User> users){
        JsonNode numberOfUsers = Json.parse("{\"numberOfUsers\":\""+ users.size()+"\"}");
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode returnJson = objectMapper.createArrayNode();
        returnJson.add(numberOfUsers);

        if(users.size()<numberPerPage){
            returnJson.add(Json.toJson(users));
        } else {
            List<User> usersPage = new ArrayList<>();
            int border = (page - 1) * numberPerPage + numberPerPage;
            if(border > users.size()){
                border = users.size();
            }
            for(int i = (page-1)*numberPerPage; i < border; i++){
                usersPage.add(users.get(i));
            }
            returnJson.add(Json.toJson(usersPage));
        }
        return returnJson;
    }


}
