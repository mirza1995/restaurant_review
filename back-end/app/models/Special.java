package models;

import javax.persistence.*;
import lombok.Getter;
@Entity
@Table(name = "specials")
public class Special{
    @Id
    @GeneratedValue
    @Column(name = "id")
    @Getter private Integer id;

    @Column(name = "title")
    @Getter private String title;

    @JoinColumn(name = "location")
    @ManyToOne
    @Getter private Location location;

    @Column(name = "number")
    @Getter private int number;

    @Column(name = "photoName")
    @Getter private String photoName;

    public Special() {
    }
}
