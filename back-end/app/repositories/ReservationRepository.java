package repositories;

import models.Reservation;
import models.Restaurant;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ReservationRepository {
    @Inject private JPAApi api;

    public void createReservation(Reservation reservation){
        api.em().persist(reservation);
    }

    public void update(Reservation reservation){
        api.em().unwrap(Session.class).update(reservation);
    }

    public List<Reservation> getReservations(String date, String hours, int id){
        return api.em().unwrap(Session.class).createCriteria(Reservation.class).add(Restrictions.eq("restaurant.id", id)).add(Restrictions.eq("date", date)).add(Restrictions.eq("hour", hours)).list();
    }

    public List<Reservation> getTodaysReservations(int id,String date){
        return api.em().unwrap(Session.class).createCriteria(Reservation.class).add(Restrictions.eq("restaurant.id", id)).add(Restrictions.eq("date", date)).list();
    }

    public List<Reservation> getAllReservations(){
        return api.em().unwrap(Session.class).createCriteria(Reservation.class).list();
    }

    public Reservation get(int id){
        return (Reservation) api.em().unwrap(Session.class).createCriteria(Reservation.class).add(Restrictions.eq("id", id)).uniqueResult();
    }

    public List<Reservation> getRestaurantReservations(int id){
        return api.em().unwrap(Session.class).createCriteria(Reservation.class).add(Restrictions.eq("restaurant.id", id)).list();

    }
    public boolean delete(int id){
        Reservation reservation = (Reservation) api.em().unwrap(Session.class).createCriteria(Reservation.class).add(Restrictions.eq("id", id)).uniqueResult();
        api.em().unwrap(Session.class).delete(reservation);
        return true;
    }
}
