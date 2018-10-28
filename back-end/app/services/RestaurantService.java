package services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import helpers.Helper;
import helpers.SortByPersons;
import models.*;
import play.libs.Json;
import repositories.*;
import scala.util.parsing.json.JSONArray;
import views.html.helper.input;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class RestaurantService {
    @Inject private RestaurantRepository repository;
    @Inject private ReservationRepository reservationRepository;
    @Inject private LocationRepository locationRepository;
    @Inject private CousineRepository cousineRepository;
    @Inject private TableRepository tableRepository;
    @Inject private RestaurantCousineRepository restaurantCousineRepository;

    private final RestaurantRepository restaurantRepository;
    private final TableRepository repositoryTable;
    private final LocationRepository repositoryLocation;
    private final ReservationRepository repositoryReservation;

    public RestaurantService(final RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
        this.repositoryTable = null;
        this.repositoryLocation = null;
        this.repositoryReservation = null;
    }

    public RestaurantService(){
        this.restaurantRepository = null;
        this.repositoryTable = null;
        this.repositoryLocation = null;
        this.repositoryReservation = null;
    }

    public RestaurantService(final TableRepository tableRepository) {
        this.repositoryTable = tableRepository;
        this.restaurantRepository = null;
        this.repositoryLocation = null;
        this.repositoryReservation = null;
    }

    public RestaurantService(final LocationRepository repository) {
        this.repositoryLocation = repository;
        this.restaurantRepository = null;
        this.repositoryTable = null;
        this.repositoryReservation = null;
    }

    public RestaurantService(final ReservationRepository repository) {
        this.repositoryReservation = repository;
        this.restaurantRepository = null;
        this.repositoryTable = null;
        this.repositoryLocation = null;
    }

    public RestaurantService(final ReservationRepository repositoryReservation, final LocationRepository repositoryLocation, final RestaurantRepository restaurantRepository, final TableRepository repositoryTable) {
        this.repositoryReservation = repositoryReservation;
        this.restaurantRepository = restaurantRepository;
        this.repositoryLocation = repositoryLocation;
        this.repositoryTable = repositoryTable;
    }

    public Restaurant addRestaurant(JsonNode json){
        JsonNode cousines = json.get("cousines");
        List<Cousine> cousinesArray = new ArrayList<>();
        for(int i = 0; i < cousines.size(); i++){
            cousinesArray.add(Json.fromJson(cousines.get(i), Cousine.class));
        }
        Restaurant restaurant = Json.fromJson(json,Restaurant.class);
        restaurant.setCousines(cousinesArray);
        repository.createRestaurant(restaurant);
        return restaurant;
    }

    public Restaurant updateRestaurant(JsonNode json){
        JsonNode cousines = json.get("cousines");
        List<Cousine> cousinesArray = new ArrayList<>();
        for(int i = 0; i < cousines.size(); i++){
            cousinesArray.add(Json.fromJson(cousines.get(i), Cousine.class));
        }
        Restaurant restaurant = Json.fromJson(json,Restaurant.class);
        restaurant.setCousines(cousinesArray);
        repository.updateRestaurant(restaurant);
        return restaurant;
    }

    public Restaurant updateRestaurant(Restaurant restaurant){
        repository.updateRestaurant(restaurant);
        return restaurant;
    }

    public Restaurant getRestaurant(int id){
        RestaurantRepository restaurantRepositoryUsed;
        if(this.restaurantRepository == null){
            restaurantRepositoryUsed = repository;
        } else {
            restaurantRepositoryUsed = restaurantRepository;
        }
        if(id > 0){
            return restaurantRepositoryUsed.get(id);
        }
        return null;
    }

    public List<Restaurant> getAllRestaurants(){
        return repository.getAllRestaurants();
    }

    public List<Restaurant> getPopularRestaurants(){
        List<Restaurant>restaurants = new ArrayList<>();
        if(this.restaurantRepository == null){
            restaurants = repository.getAllRestaurants();
        } else {
            restaurants = restaurantRepository.getAllRestaurants();
        }

        List<Restaurant> popularRestaurants = new ArrayList<>();
        List<Double> popularity = restaurants.stream()
                .map(restaurant ->{
                    int mark=restaurant.getMark();
                    int votes=restaurant.getVotes();
                    int priceRange=restaurant.getPriceRange();
                    int booked=restaurant.getBooked();
                    return votes*mark/priceRange+booked/(2.0);
                })
                .collect(Collectors.toList());
        int size = restaurants.size();
        for(int i = 0; i < size; i++){
            int maxIndex = Helper.findIndexOfMaximum(popularity);
            popularRestaurants.add(restaurants.get(maxIndex));
            popularity.remove(maxIndex);
            restaurants.remove(maxIndex);
        }
        return popularRestaurants;
    }

    public List<Restaurant> checkRestaurantCousines(String[] cousines, List<Restaurant> restaurants){
        if(cousines != null){
            cousines = Helper.convertToUpperCase(cousines);
            List<String> cousinesList = Arrays.asList(cousines);
            List<Restaurant> restaurantsWithCousine = restaurants.stream()
                    .filter(restaurant ->
                            restaurant.getCousines().stream()
                                    .filter(cousine -> cousinesList.contains(cousine.getName()))
                                    .collect(Collectors.toList())
                                    .size() > 0)
                    .collect(Collectors.toList());
            return restaurantsWithCousine;
        }
        return restaurants;
    }

    public JsonNode search(String cousinesString, int page, Restaurant restaurantToSearch){
        String[] cousines = null;
        if(cousinesString != null && cousinesString != ""){
            cousines =  cousinesString.split(",");
        }
        int numberPerPage=6;

        String restaurantNameToSearch = restaurantToSearch.getRestaurantName().toLowerCase();
        List<Restaurant> restaurants = checkRestaurantCousines(cousines,repository.searchRestaurant(restaurantNameToSearch,restaurantToSearch,page,numberPerPage));
        if(restaurants.size() > 0){
            return getRestaurantsPage(page,numberPerPage,restaurants);
        }
        return null;

    }

    public JsonNode getRestaurantsPage(int page, int numberPerPage, List<Restaurant> restaurants){
        JsonNode numberOfRestaurants = Json.parse("{\"numberOfRestaurants\":\""+restaurants.size()+"\"}");
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode returnJson = objectMapper.createArrayNode();
        returnJson.add(numberOfRestaurants);

        if(restaurants.size()<numberPerPage){
            returnJson.add(Json.toJson(restaurants));
        } else {
            List<Restaurant> restaurantsPage = new ArrayList<>();
            int border = (page - 1)*numberPerPage + numberPerPage;
            if(border > restaurants.size()){
                border = restaurants.size();
            }
            for(int i = (page-1)*numberPerPage; i < border; i++){
                restaurantsPage.add(restaurants.get(i));
            }
            returnJson.add(Json.toJson(restaurantsPage));
        }
        return returnJson;
    }


    public List<Restaurant> getRestaurantsPage (int page, int numberPerPage){
        RestaurantRepository restaurantRepositoryUsed;
        if(this.restaurantRepository == null){
            restaurantRepositoryUsed = repository;
        } else {
            restaurantRepositoryUsed = restaurantRepository;
        }
        return restaurantRepositoryUsed.getRestaurantsPage(page,numberPerPage);
    }

    public JsonNode findTables(String date, String searchName, String hours, int persons){
        ReservationRepository reservationRepositoryUsed;
        LocationRepository locationRepositoryUsed;
        RestaurantRepository restaurantRepositoryUsed;
        if(this.repositoryLocation == null){
            locationRepositoryUsed = locationRepository;
        } else {
            locationRepositoryUsed = repositoryLocation;
        }
        if(this.repositoryReservation == null){
            reservationRepositoryUsed = reservationRepository;
        } else {
            reservationRepositoryUsed = repositoryReservation;
        }
        if(this.restaurantRepository == null){
            restaurantRepositoryUsed = repository;
        } else {
            restaurantRepositoryUsed = restaurantRepository;
        }

        Location location = locationRepositoryUsed.getByName(searchName);

        List<Restaurant> restaurants = restaurantRepositoryUsed.filterRestaurants(searchName,location);
        if(restaurants == null || restaurants.size()==0){

            List<Restaurant> allRestaurants = restaurantRepositoryUsed.getAllRestaurants();
            List<Restaurant> restaurantsWithCousine = checkRestaurantCousines(searchName.split(","),allRestaurants);
            if(restaurantsWithCousine.size()>0 && restaurantsWithCousine.size() != allRestaurants.size()){
                restaurants = restaurantsWithCousine;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode restaurantsToReturn = objectMapper.createArrayNode();
        restaurants.stream().map(restaurant -> {
            JsonNode restaurantJson = Json.toJson(restaurant);
            Integer restaurant_id = restaurant.getId();
            List<Reservation> reservations = reservationRepositoryUsed.getTodaysReservations(restaurant_id,date);;

            JsonNode todaysReservationsNumber = Json.parse("{\"reservationsToday\":"+reservations.size()+"}");
            //JsonNode searchInformationForTables = Json.parse("{\"id\":\""+restaurant_id+"\",\"searchName\":\""+searchName+"\",\"hours\":\""+hours+"\",\"date\":\""+date+"\",\"persons\":\""+persons+"\",\"tables\":"+restaurant.getTables()+"}");
            JsonNode avaliableTimes = findTable(date, searchName, hours, restaurant_id ,persons);
            ArrayNode jsonOfRestaurants = objectMapper.createArrayNode();
            jsonOfRestaurants.add(restaurantJson);
            jsonOfRestaurants.add(avaliableTimes);
            jsonOfRestaurants.add(todaysReservationsNumber);
            restaurantsToReturn.add(jsonOfRestaurants);
            return null;
        }).collect(Collectors.toList());

        return Json.toJson(restaurantsToReturn);
    }

    public int getNumberOfRestaurants(){
        return repository.getNumberOfRestaurants().intValue();
    }

    public JsonNode findTable(String date, String searchName, String hours, int id, int persons){
        ReservationRepository reservationRepositoryUsed;
        if(this.repositoryReservation == null){
            reservationRepositoryUsed = reservationRepository;
        } else {
            reservationRepositoryUsed = repositoryReservation;
        }

        //Hours are in format X:XX AM/PM
        String[] hoursArray = hours.split(":");
        String hoursAmPm = hoursArray[1].split(" ")[1];
        boolean halfHour = false;
        if(hoursArray[1].split(" ")[0].equals("30")){
            halfHour = true;
        }
        int wholeHour = Integer.parseInt(hoursArray[0]);
        int numberOfReservationFound = 0;
        ArrayList<String> hoursAvaliable = new ArrayList<>();
        //Loop for finding 4 avaliable times for reservation
        int numberOfFreeTables = 0;
        while(numberOfReservationFound < 4){
            String hoursToCheck;
            if(halfHour){
                hoursToCheck = String.valueOf(wholeHour) + ":30 " + hoursAmPm;
                wholeHour++;
            } else {
                hoursToCheck = String.valueOf(wholeHour) + ":00 " + hoursAmPm;
            }
            List<Reservation> reservations = reservationRepositoryUsed.getReservations(date,hoursToCheck,id);
            if(reservations != null && reservations.size() > 0){
                List<RestaurantTable> freeTables = getFreeTables(reservations);
                if(freeTables != null && freeTables.size() > 0 && numberOfFreeTables == 0){
                    for(int i = 0; i < freeTables.size(); i++){
                        numberOfFreeTables += freeTables.get(i).getAmmount();
                    }
                }
            }
            if(reservations == null || reservations.size() == 0 || checkReservation(persons,getFreeTables(reservations))){
                hoursAvaliable.add(hoursToCheck);
                numberOfReservationFound++;
            }

            //Border cases
            if(wholeHour == 12 && hoursAmPm.equals("PM")){
                break;
            } else if(wholeHour == 13 && hoursAmPm.equals("AM")){
                wholeHour = 1;
                hoursAmPm = "PM";
            }

            halfHour=!halfHour;
        }
        if(numberOfFreeTables == 0){
            List<RestaurantTable>allTables = tableRepository.getRestaurantTables(id);
            for(int i = 0; i < allTables.size(); i++){
                numberOfFreeTables += allTables.get(i).getAmmount();
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode returnJson = objectMapper.createArrayNode();
        JsonNode numberOfTables = Json.parse("{\"numberOfFreeTables\":\""+ numberOfFreeTables+"\"}");
        returnJson.add(numberOfTables);
        returnJson.add(Json.toJson(hoursAvaliable));
        return returnJson;
    }

    public List<RestaurantTable> getFreeTables(List<Reservation> reservations){
        int restaurant_id = reservations.get(0).getRestaurant().getId();
        List<RestaurantTable> tables;
        if(this.repositoryTable == null){
            tables = tableRepository.getRestaurantTables(restaurant_id);
        } else {
            tables = repositoryTable.getRestaurantTables(restaurant_id);
        }

        Collections.sort(tables, new SortByPersons());
        boolean tableFound = false;
        List<Reservation> reservationsLeft = new ArrayList<>();
        for(int i = 0; i < reservations.size(); i++){
            tableFound = false;
            for(int j = 0; j < tables.size(); j++){
                if(tables.get(j).getAmmount() > 0 && tables.get(j).getPersons() >= reservations.get(i).getPersons()){
                    tables.get(j).descreaseAmmount();
                    tableFound = true;
                    break;
                }
            }
            if(!tableFound){
                reservationsLeft.add(reservations.get(i));
            }
        }

        for(int i = 0; i < reservationsLeft.size(); i++){
            int persons = 0;
            tableFound = false;
            Reservation reservation = reservationsLeft.get(i);
            for(int j = 0; j < tables.size(); j++) {
                if(tables.get(j).getAmmount() == 0 || tables.get(j).getShape().equals("round")){
                    continue;
                }
                while(tables.get(j).getAmmount() > 0){
                    persons += tables.get(j).getPersons();
                    tables.get(j).descreaseAmmount();
                    if(persons >= reservation.getPersons()){
                        tableFound = true;
                        break;
                    }
                }
                if(tableFound){
                    break;
                }
            }
        }
        return tables;
    }

    public boolean checkReservation(int reservationPersons, List<RestaurantTable> tables){
        int persons = 0;
        for(int i = 0; i < tables.size(); i++){
            RestaurantTable table = tables.get(i);
            if(table.getAmmount() > 0){
                if(table.getPersons() >= reservationPersons){
                    return true;
                } else if(table.getShape().equals("square")){
                    while(table.getAmmount() > 0){
                        persons += table.getPersons();
                        if(persons >= reservationPersons){
                            return true;
                        }
                        table.descreaseAmmount();
                    }
                }
            }
        }
        return false;
    }

    public boolean deleteRestaurant(int id){
        boolean restaurantDeleted = repository.delete(id);
        if(restaurantDeleted){
            return tableRepository.deleteRestaurantTables(id);

        }
        return false;
    }

    public JsonNode getCousines(){
        return Json.toJson(cousineRepository.getAllCousines());
    }

    public List<Cousine> getCousinesPage(int page){
        return repository.getCousinesPage(page,12);
    }

    public JsonNode getCousinesPage(String searchCriteria, int page){
        List<Cousine> cousines;
        if(searchCriteria.equals("Empty")){
            return getCousinesWithNoSearch(page);
        } else {
            cousines = repository.searchCousine(searchCriteria);
            JsonNode returnJson = makeCousinePage(page,12,cousines);
            return returnJson;
        }
    }

    public JsonNode makeCousinePage(int page, int numberPerPage, List<Cousine> cousines){
        JsonNode numberOfCousines = Json.parse("{\"numberOfCousines\":\""+ cousines.size()+"\"}");
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode returnJson = objectMapper.createArrayNode();
        returnJson.add(numberOfCousines);

        if(cousines.size()<numberPerPage){
            returnJson.add(Json.toJson(cousines));
        } else {
            List<Cousine> cousinesPage = new ArrayList<>();
            int border = (page - 1)*numberPerPage + numberPerPage;
            if(border > cousines.size()){
                border = cousines.size();
            }
            for(int i = (page-1)*numberPerPage; i < border; i++){
                cousinesPage.add(cousines.get(i));
            }
            returnJson.add(Json.toJson(cousinesPage));
        }
        return returnJson;
    }

    public JsonNode getCousinesWithNoSearch (int page){
        List<Cousine> cousines = getCousinesPage(page);
        JsonNode numberOfCousines = Json.parse("{\"numberOfCousines\":\""+repository.getNumberOfCousines()+"\"}");
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode returnJson = objectMapper.createArrayNode();
        returnJson.add(numberOfCousines);
        returnJson.add(Json.toJson(cousines));
        return returnJson;
    }

    public JsonNode searchCousine(String searchCriteria){
        List<Cousine> cousines = repository.searchCousine(searchCriteria);
        return Json.toJson(makeCousinePage(1,12,cousines));
    }

    //Restaurant Admin Site

    public JsonNode getRestaurantsPageAdmin(String searchCriteria, int page){
        RestaurantRepository restaurantRepositoryUsed;
        if(this.restaurantRepository == null){
            restaurantRepositoryUsed = repository;
        } else {
            restaurantRepositoryUsed = restaurantRepository;
        }
        List<Restaurant> restaurants;
        if(searchCriteria.equals("Empty")){
            return getRestaurantsWithNoSearch(page);
        } else {
            restaurants = restaurantRepositoryUsed.searchRestaurantAdmin(searchCriteria);
            JsonNode returnJson = getRestaurantsPage(page,12,restaurants);
            return returnJson;
        }
    }

    public JsonNode getRestaurantsWithNoSearch (int page){
        RestaurantRepository restaurantRepositoryUsed;
        if(this.restaurantRepository == null){
            restaurantRepositoryUsed = repository;
        } else {
            restaurantRepositoryUsed = restaurantRepository;
        }
        List<Restaurant> restaurants = getRestaurantsPage(page,12);
        JsonNode numberOfRestaurants = Json.parse("{\"numberOfRestaurants\":\""+restaurantRepositoryUsed.getNumberOfRestaurants()+"\"}");
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode returnJson = objectMapper.createArrayNode();
        returnJson.add(numberOfRestaurants);
        returnJson.add(Json.toJson(restaurants));
        return returnJson;
    }

    public JsonNode searchRestaurantAdmin(String searchCriteria){
        List<Restaurant> restaurants = repository.searchRestaurantAdmin(searchCriteria);
        return Json.toJson(getRestaurantsPage(1,12,restaurants));
    }

    public String updateCousine(Cousine cousine){
        if(cousine == null){
            return "Invalid";
        }
        repository.updateCousine(cousine);
        return String.valueOf(cousine.getId());
    }

    public String createCousine(Cousine cousine){
        if(cousine == null){
            return "Invalid";
        }
        repository.createCousine(cousine);
        return String.valueOf(cousine.getId());
    }

    public Cousine getCousine(int id){
        if(id > 0){
            return repository.getCousine(id);
        }
        return null;
    }

    public boolean deleteCousine(int id){
        return repository.deleteCousine(id);
    }

    public List<Restaurant> closestRestaurants(double latitude, double longitude){
        return repository.getClosestRestaurants(latitude,longitude).stream().limit(6).collect(Collectors.toList());
    }

    public List<Restaurant> getClosestRestaurantsForMap(double latitude, double longitude){
        List<Restaurant> restaurants = repository.getClosestRestaurants(latitude,longitude);
        return restaurants.stream()
                .filter(restaurant -> restaurant.getLocation().getLocationName().equals(restaurants.get(0).getLocation().getLocationName()))
                .collect(Collectors.toList());
    }

    public JsonNode getCousinesForCarousel(){
        List<Cousine> cousines = cousineRepository.getAllCousines().stream().limit(9).collect(Collectors.toList());
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode returnJson = objectMapper.createArrayNode();
        for(int i = 0; i < cousines.size(); i++){
            JsonNode cousineElement = Json.parse("{\"numberOfRestaurants\":\""+restaurantCousineRepository.getNumberOfRestaurantsForCousine(cousines.get(i).getId())+
                    "\",\"cousineName\":\""+cousines.get(i).getName()+
                    "\",\"photoName\":\""+cousines.get(i).getPhoto()+"\"}");
            returnJson.add(cousineElement);
        }
        return returnJson;
    }

}
