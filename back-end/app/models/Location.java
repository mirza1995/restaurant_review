package models;

import lombok.Getter;
import lombok.Setter;
import org.postgresql.geometric.PGpath;
import org.postgresql.geometric.PGpoint;
import play.data.validation.Constraints;

import javax.persistence.*;

@Entity
@Table(name = "locations")
public class Location{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter @Setter private Integer id;

    @Constraints.Required
    @Column(name = "location")
    @Getter @Setter private String locationName;

    @Constraints.Required
    @Column(name = "number")
    @Getter @Setter
    private int numberOfRestaurants;

    @Column(name = "points")
    @Getter @Setter private PGpoint[] points;

    public Location() {
    }

    public Location(String locationName, int numberOfRestaurants) {
        this.locationName = locationName;
        this.numberOfRestaurants = numberOfRestaurants;
        this.points = null;
    }

    public Location(@Constraints.Required String locationName, @Constraints.Required int numberOfRestaurants, PGpoint[] rectangle) {
        this.locationName = locationName;
        this.numberOfRestaurants = numberOfRestaurants;
        this.points = rectangle;
    }
}