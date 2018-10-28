package models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter @Setter private Integer id;

    @JoinColumn(name = "restaurant_id")
    @ManyToOne
    @Getter @Setter private Restaurant restaurant;

    @Column(name = "persons")
    @Getter @Setter private int persons;

    @Column(name = "date")
    @Getter @Setter private String date;

    @Column(name = "hour")
    @Getter @Setter private String hour;

    public Reservation(Restaurant restaurant, int persons, String date, String hour) {
        this.restaurant = restaurant;
        this.persons = persons;
        this.date = date;
        this.hour = hour;
    }

    public Reservation() {
    }
}
