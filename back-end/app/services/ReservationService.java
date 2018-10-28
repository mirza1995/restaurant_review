package services;

import com.fasterxml.jackson.databind.JsonNode;
import models.Reservation;
import models.Restaurant;
import models.RestaurantTable;
import repositories.ReservationRepository;
import repositories.TableRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ReservationService {
    @Inject private ReservationRepository repository;
    @Inject private RestaurantService restaurantService;

    public Reservation addReservation(Reservation reservation){
        repository.createReservation(reservation);
        return reservation;
    }
    public Reservation updateReservation(Reservation reservation){
        repository.update(reservation);
        return reservation;
    }
    public List<Reservation> getTodaysReservations(int id, String date){
        return repository.getTodaysReservations(id,date);
    }

    public Reservation makeReservation(Reservation reservation, int id){
        Restaurant restaurant = restaurantService.getRestaurant(id);
        reservation.setRestaurant(restaurant);
        restaurant.increaseBooked();
        restaurantService.updateRestaurant(restaurant);
        return addReservation(reservation);
    }

    public Reservation getReservation(int id){
        return repository.get(id);
    }

    public List<Reservation> getAllReservations(){
        return repository.getAllReservations();
    }

    public boolean deleteReservation(int id){
        return repository.delete(id);
    }

}
