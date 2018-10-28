package models;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "menus")
public class Meal {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter @Setter
    private Integer id;

    @JoinColumn(name = "restaurant_id")
    @ManyToOne
    @Getter @Setter private Restaurant restaurant;

    @Column(name = "name")
    @Getter private String name;

    @Column(name = "type")
    @Getter private String type;

    @Column(name = "description")
    @Getter private String description;

    @Column(name = "largePrice")
    @Getter private float largePrice;

    @Column(name = "smallPrice")
    @Getter private float smallPrice;

    @Column(name = "mealType")
    @Getter private String mealType;
}
