
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.*;
import org.junit.Test;
import play.libs.Json;
import repositories.*;
import services.LocationService;
import services.PhotoService;
import services.RestaurantService;
import services.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UnitTest {

    @Test
    public void testCheckReservation() {
        List<RestaurantTable> tables = new ArrayList<>();
        List<Cousine>cousines = new ArrayList<>();
        cousines.add(new Cousine("Bosnian","http://novovrijeme.ba/wp-content/uploads/34681.jpg"));
        Restaurant restaurant = new Restaurant("Chipas","Chipas je najbolji",45.432,43.3213,2,500,3,"/assets/images/chipas.png","/assets/images/reservation-page-background.png",new Location("Sarajevo",8),500,10,cousines);
        tables.add(new RestaurantTable(restaurant,5,"square",5));
        int reservationPersons = 8;

        RestaurantRepository repositoryMock = mock(RestaurantRepository.class);

        RestaurantService restaurantService = new RestaurantService(repositoryMock);
        assertTrue(restaurantService.checkReservation(reservationPersons,tables));
    }



    @Test
    public void testGetPopularRestaurants(){
        List<Restaurant>restaurants = new ArrayList<>();
        List<Cousine>cousines = new ArrayList<>();
        cousines.add(new Cousine("Bosnian","http://novovrijeme.ba/wp-content/uploads/34681.jpg"));
        Restaurant restaurant = new Restaurant("Chipas","Chipas je najbolji",45.432,43.3213,2,500,3,"/assets/images/chipas.png","/assets/images/reservation-page-background.png",new Location("Sarajevo",8),500,10,cousines);
        restaurant.setId(5);
        restaurants.add(restaurant);
        restaurants.add(restaurant);
        RestaurantRepository repositoryMock = mock(RestaurantRepository.class);
        when(repositoryMock.getAllRestaurants()).thenReturn(restaurants);

        RestaurantService restaurantService = new RestaurantService(repositoryMock);
        assertTrue(restaurantService.getPopularRestaurants().size() > 0);
    }

    @Test
    public void testGetFreeTables(){
        List<RestaurantTable> tables = new ArrayList<>();
        List<Cousine>cousines = new ArrayList<>();
        cousines.add(new Cousine("Bosnian","http://novovrijeme.ba/wp-content/uploads/34681.jpg"));
        Restaurant restaurant = new Restaurant("Chipas","Chipas je najbolji",45.432,43.3213,2,500,3,"/assets/images/chipas.png","/assets/images/reservation-page-background.png",new Location("Sarajevo",8),500,10,cousines);
        restaurant.setId(1);
        tables.add(new RestaurantTable(restaurant,5,"square",5));
        tables.add(new RestaurantTable(restaurant,2,"square",5));
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation(restaurant,5,"Jul 10,2018","12:00 AM"));
        reservations.add(new Reservation(restaurant,6,"Jul 10,2018","12:00 AM"));
        reservations.add(new Reservation(restaurant,8,"Jul 10,2018","12:00 AM"));


        TableRepository repositoryMock = mock(TableRepository.class);
        when(repositoryMock.getRestaurantTables(anyInt())).thenReturn(tables);

        RestaurantService restaurantService = new RestaurantService(repositoryMock);
        assertTrue(restaurantService.getFreeTables(reservations).size() > 0);
    }

    @Test
    public void testFindTables(){
        Location location = new Location("Sarajevo",6);
        List<Cousine>cousines = new ArrayList<>();
        cousines.add(new Cousine("Bosnian","http://novovrijeme.ba/wp-content/uploads/34681.jpg"));
        Restaurant restaurant = new Restaurant("Chipas","Chipas je najbolji",45.432,43.3213,2,500,3,"/assets/images/chipas.png","/assets/images/reservation-page-background.png",location,500,10,cousines);
        List<Restaurant>allRestaurants = new ArrayList<>();
        allRestaurants.add(restaurant);
        restaurant = new Restaurant("Woki","Woki je najbolji",45.432,43.3213,2,500,3,"/assets/images/chipas.png","/assets/images/reservation-page-background.png",location,500,10,cousines);
        allRestaurants.add(restaurant);
        List<Restaurant> filteredRestaurants = new ArrayList<>();
        filteredRestaurants.add(restaurant);

        LocationRepository repositoryLocationMock = mock(LocationRepository.class);
        when(repositoryLocationMock.getByName(anyString())).thenReturn(location);

        RestaurantRepository repositoryRestaurantMock = mock(RestaurantRepository.class);
        when(repositoryRestaurantMock.getAllRestaurants()).thenReturn(allRestaurants);
        when(repositoryRestaurantMock.filterRestaurants(anyString(),any(Location.class))).thenReturn(filteredRestaurants);

        Reservation reservation1 = new Reservation(restaurant,5,"Jul 10,2018","12:30 AM");
        Reservation reservation2 = new Reservation(restaurant,7,"Jul 10,2018","10:30 AM");
        List<Reservation>reservations = new ArrayList<>();
        reservations.add(reservation1);
        reservations.add(reservation2);

        ReservationRepository repositoryReservationMock = mock(ReservationRepository.class);
        when(repositoryReservationMock.getTodaysReservations(anyInt(),anyString())).thenReturn(reservations);
        when(repositoryReservationMock.getReservations(anyString(),anyString(),anyInt())).thenReturn(reservations);

        List<RestaurantTable>tables = new ArrayList<>();
        tables.add(new RestaurantTable(restaurant,5,"square",5));
        tables.add(new RestaurantTable(restaurant,2,"square",5));
        TableRepository repositoryTableMock = mock(TableRepository.class);
        when(repositoryTableMock.getRestaurantTables(anyInt())).thenReturn(tables);

        RestaurantService restaurantService = new RestaurantService(repositoryReservationMock, repositoryLocationMock, repositoryRestaurantMock,repositoryTableMock);
        assertTrue(restaurantService.findTables("Jul 10,2018","Woki","12:30 AM",5).size() > 0);
    }

    @Test
    public void testGetRestaurant(){
        Location location = new Location("Sarajevo",6);
        List<Cousine>cousines = new ArrayList<>();
        cousines.add(new Cousine("Bosnian","http://novovrijeme.ba/wp-content/uploads/34681.jpg"));
        Restaurant restaurant = new Restaurant("Chipas","Chipas je najbolji",45.432,43.3213,2,500,3,"/assets/images/chipas.png","/assets/images/reservation-page-background.png",location,500,10,cousines);
        restaurant.setId(5);
        RestaurantRepository repositoryRestaurantMock = mock(RestaurantRepository.class);
        when(repositoryRestaurantMock.get(anyInt())).thenReturn(restaurant);

        RestaurantService restaurantService = new RestaurantService(repositoryRestaurantMock);
        assertEquals(restaurant,restaurantService.getRestaurant(5));
    }

    @Test
    public void testGetRestaurantsPage(){
        List<Restaurant>restaurants = new ArrayList<>();
        List<Cousine>cousines = new ArrayList<>();
        cousines.add(new Cousine("Bosnian","http://novovrijeme.ba/wp-content/uploads/34681.jpg"));
        Restaurant restaurant = new Restaurant("Chipas","Chipas je najbolji",45.432,43.3213,2,500,3,"/assets/images/chipas.png","/assets/images/reservation-page-background.png",new Location("Sarajevo",8),500,10,cousines);
        restaurant.setId(5);
        restaurants.add(restaurant);
        restaurants.add(restaurant);

        RestaurantService restaurantService = new RestaurantService();
        assertTrue(restaurantService.getRestaurantsPage(6,6,restaurants).size() > 0);
    }

    @Test
    public void testGetRestaurantsPageAdmin(){
        List<Restaurant>restaurants = new ArrayList<>();
        List<Cousine>cousines = new ArrayList<>();
        cousines.add(new Cousine("Bosnian","http://novovrijeme.ba/wp-content/uploads/34681.jpg"));
        Restaurant restaurant = new Restaurant("Chipas","Chipas je najbolji",45.432,43.3213,2,500,3,"/assets/images/chipas.png","/assets/images/reservation-page-background.png",new Location("Sarajevo",8),500,10,cousines);
        restaurant.setId(5);
        restaurants.add(restaurant);
        restaurants.add(restaurant);

        RestaurantRepository repositoryRestaurantMock = mock(RestaurantRepository.class);
        when(repositoryRestaurantMock.searchRestaurantAdmin(anyString())).thenReturn(restaurants);
        when(repositoryRestaurantMock.getNumberOfRestaurants()).thenReturn(restaurants.size());

        RestaurantService restaurantService = new RestaurantService(repositoryRestaurantMock);
        assertTrue(restaurantService.getRestaurantsPageAdmin("Chipas",6).size() > 0);
    }

    @Test
    public void testGetRestaurantsWithNoSearch(){
        List<Restaurant>restaurants = new ArrayList<>();
        List<Cousine>cousines = new ArrayList<>();
        cousines.add(new Cousine("Bosnian","http://novovrijeme.ba/wp-content/uploads/34681.jpg"));
        Restaurant restaurant = new Restaurant("Chipas","Chipas je najbolji",45.432,43.3213,2,500,3,"/assets/images/chipas.png","/assets/images/reservation-page-background.png",new Location("Sarajevo",8),500,10,cousines);
        restaurant.setId(5);
        restaurants.add(restaurant);
        restaurants.add(restaurant);

        RestaurantRepository repositoryRestaurantMock = mock(RestaurantRepository.class);
        when(repositoryRestaurantMock.getNumberOfRestaurants()).thenReturn(restaurants.size());
        when(repositoryRestaurantMock.getRestaurantsPage(anyInt(),anyInt())).thenReturn(restaurants);

        JsonNode numberOfRestaurants = Json.parse("{\"numberOfRestaurants\":\""+restaurants.size()+"\"}");
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode returnJson = objectMapper.createArrayNode();
        returnJson.add(numberOfRestaurants);
        returnJson.add(Json.toJson(restaurants));

        RestaurantService restaurantService = new RestaurantService(repositoryRestaurantMock);
        assertEquals(returnJson,restaurantService.getRestaurantsWithNoSearch(6));
    }

    @Test
    public void testGetRestaurantPhotos(){
        List<Photo> photos = new ArrayList<>();
        List<Cousine>cousines = new ArrayList<>();
        cousines.add(new Cousine("Bosnian","http://novovrijeme.ba/wp-content/uploads/34681.jpg"));
        Restaurant restaurant = new Restaurant("Chipas","Chipas je najbolji",45.432,43.3213,2,500,3,"/assets/images/chipas.png","/assets/images/reservation-page-background.png",new Location("Sarajevo",8),500,10,cousines);
        restaurant.setId(1);
        photos.add(new Photo(restaurant,"/assets/images/chipas.png"));
        PhotoRepository repositoryMock = mock(PhotoRepository.class);
        when(repositoryMock.getRestaurantPhotos(anyInt())).thenReturn(photos);

        PhotoService service = new PhotoService(repositoryMock);
        assertTrue(service.getRestaurantPhotos(restaurant.getId(),1).size() > 0);
    }

    @Test
    public void testMakeUsersPage(){
        User user = new User("Mirza","Alispahic","malispahic4@gmail.com","3232132132","Bosnia and Herzegovina","Zenica","1234","admin");
        List<User> users = new ArrayList<>();
        users.add(user);
        UserService service = new UserService();
        assertTrue(service.makeUsersPage(1,5,users).size() > 0);
    }

    @Test
    public void testGetUsersPage(){
        User user = new User("Mirza","Alispahic","malispahic4@gmail.com","3232132132","Bosnia and Herzegovina","Zenica","1234","admin");
        List<User> users = new ArrayList<>();
        users.add(user);
        UserRepository repositoryMock = mock(UserRepository.class);
        when(repositoryMock.getUsersPage(anyInt(),anyInt())).thenReturn(users);
        UserService service = new UserService(repositoryMock);
        assertTrue(service.getUsersPage(1).size() > 0);
    }

    @Test
    public void getUsersPageWithNoSearch(){
        User user = new User("Mirza","Alispahic","malispahic4@gmail.com","3232132132","Bosnia and Herzegovina","Zenica","1234","admin");
        List<User> users = new ArrayList<>();
        users.add(user);
        UserRepository repositoryMock = mock(UserRepository.class);
        when(repositoryMock.getUsersPage(anyInt(),anyInt())).thenReturn(users);
        when(repositoryMock.getNumberOfUsers()).thenReturn(users.size());

        UserService service = new UserService(repositoryMock);
        assertTrue(service.getUsersPageWithNoSearch(1).size() > 0);
    }

    @Test
    public void getUsersPage(){
        User user = new User("Mirza","Alispahic","malispahic4@gmail.com","3232132132","Bosnia and Herzegovina","Zenica","1234","admin");
        List<User> users = new ArrayList<>();
        users.add(user);
        UserRepository repositoryMock = mock(UserRepository.class);
        when(repositoryMock.searchUser(anyString())).thenReturn(users);
        when(repositoryMock.getNumberOfUsers()).thenReturn(users.size());

        UserService service = new UserService(repositoryMock);
        assertTrue(service.getUsersPage("Mirza",1).size() > 0);
    }

    @Test
    public void makeLocationPage(){
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("Sarajevo",5));
        locations.add(new Location("Zenica",5));

        LocationService service = new LocationService();
        assertTrue(service.makeLocationPage(1,4,locations).size() > 0);
    }

    @Test
    public void getLocationsWithNoSearch(){
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("Sarajevo",5));
        locations.add(new Location("Zenica",5));

        LocationRepository repositoryMock = mock(LocationRepository.class);
        when(repositoryMock.getNumberOfLocations()).thenReturn(locations.size());
        when(repositoryMock.getLocationsPage(anyInt(),anyInt())).thenReturn(locations);

        LocationService service = new LocationService(repositoryMock);
        assertTrue(service.getLocationsWithNoSearch(1).size() > 0);
    }


}
