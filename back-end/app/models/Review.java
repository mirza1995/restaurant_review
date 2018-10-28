package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    @Getter @Setter private Integer id;

    @JoinColumn(name="restaurant_id")
    @ManyToOne
    @Getter @Setter private Restaurant restaurant;

    @JoinColumn(name="user_id")
    @ManyToOne
    @Getter @Setter private User user;

    @Column(name = "rating")
    @Getter @Setter private int rating;

    @Column(name = "description")
    @Getter @Setter private String description;
}
