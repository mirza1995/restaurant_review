package models;


import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "restaurants")
public class Restaurant{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter @Setter public int id;

    @Column(name = "restaurantName")
    @Getter @Setter private String restaurantName;

    @Column(name = "description")
    @Getter @Setter private String description;

    @Column(name = "latitude")
    @Getter @Setter private double  latitude;

    @Column(name = "longitude")
    @Getter @Setter private double longitude;

    @Column(name = "mark")
    @Getter @Setter private int mark;

    @Column(name = "votes")
    @Getter @Setter private int votes;

    @Column(name = "priceRange")
    @Getter @Setter private int priceRange;

    @Column(name = "imageFileName")
    @Getter @Setter private String imageFileName;

    @Column(name = "coverFileName")
    @Getter @Setter private String coverFileName;

    @JoinColumn(name = "location")
    @ManyToOne
    @Getter @Setter private Location location;

    @Column(name = "booked")
    @Getter @Setter private int booked;

    @Column(name = "tables")
    @Getter @Setter private int tables;

    @ManyToMany
    @JoinTable(name = "restaurant_cousines", joinColumns = { @JoinColumn(name = "restaurant_id") }, inverseJoinColumns = { @JoinColumn(name = "cousine_id") })
    @Getter @Setter private List<Cousine> cousines;

    public Restaurant(String restaurantName, String description, double latitude, double longitude, int mark, int votes, int priceRange, String imageFileName, String coverFileName, Location location, int booked, int tables, List<Cousine> cousines) {
        this.restaurantName = restaurantName;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mark = mark;
        this.votes = votes;
        this.priceRange = priceRange;
        this.imageFileName = imageFileName;
        this.coverFileName = coverFileName;
        this.location = location;
        this.booked = booked;
        this.tables = tables;
        this.cousines = cousines;
    }

    public Restaurant() {
    }

    public void increaseBooked(){
        this.booked=this.getBooked()+1;
    }
    public void increaseVotes(){
        this.votes=this.getVotes()+1;
    }
}
