package models;

import lombok.Getter;
import javax.persistence.*;

@Entity
@Table(name = "cousines")
public class Cousine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter private Integer id;

    @Column(name = "name")
    @Getter private String name;

    @Column(name = "photo")
    @Getter private String photo;

    public Cousine(String name, String photo) {
        this.photo = photo;
        this.name = name;
    }

    public Cousine() {
    }
}
