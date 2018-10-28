package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Reservation;
import models.Restaurant;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ReservationService;
import javax.inject.Inject;
import java.util.List;

public class ReservationController extends Controller {
    @Inject private ReservationService reservationService;

    @Transactional
    public Result reservation(){
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        Reservation reservation = new Reservation();
        int id = Integer.parseInt(json.get("restaurant").asText());
        reservation.setPersons(Integer.parseInt(json.get("persons").asText()));
        reservation.setHour(json.get("hour").asText());
        reservation.setDate(json.get("date").asText());
        Reservation reservationCompleted = reservationService.makeReservation(reservation, id);
        return ok(Json.toJson(reservationCompleted));
    }
    @Transactional(readOnly = true)
    public Result getTodaysReservations() {
        JsonNode json = request().body().asJson();
        if (json == null){
            return badRequest();
        }
        List<Reservation> todaysReservations = reservationService.getTodaysReservations(Integer.parseInt(json.get("id").asText()),json.get("date").asText());
        if(todaysReservations == null){
            return ok(Json.toJson(0));
        }
        return ok(Json.toJson(todaysReservations.size()));
    }
}
