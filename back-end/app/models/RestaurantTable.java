package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Comparator;

@Entity
@Table(name = "tables")
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter @Setter
    private Integer id;

    @JoinColumn(name = "restaurant_id")
    @ManyToOne
    @Getter @Setter private Restaurant restaurant;

    @Column(name = "persons")
    @Getter @Setter private int persons;

    @Column(name = "shape")
    @Getter @Setter private String shape;

    @Column(name = "ammount")
    @Getter @Setter private int ammount;

    public RestaurantTable(Restaurant restaurant, int persons, String shape, int ammount) {
        this.restaurant = restaurant;
        this.persons = persons;
        this.shape = shape;
        this.ammount = ammount;
    }

    public RestaurantTable() {
    }

    public void descreaseAmmount(){
        if(this.getAmmount() > 0) {
            this.setAmmount(this.getAmmount() - 1);
        }
    }
}
