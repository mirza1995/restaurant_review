package models;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter private Integer id;

    @JoinColumn(name = "restaurant_id")
    @ManyToOne
    @Getter @Setter private Restaurant restaurant;

    @Column(name = "url")
    @Getter @Setter private String url;

    public Photo(Restaurant restaurant, String url) {
        this.restaurant = restaurant;
        this.url = url;
    }

    public Photo() {
    }
}
