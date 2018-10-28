package models;
import lombok.Getter;
import javax.persistence.*;

@Entity
@Table(name = "restaurant_cousines")
public class RestaurantCousine {
    @Id
    @Getter private Integer id;

    @Column(name = "restaurant_id")
    @Getter private Integer restaurant_id;

    @Column(name = "cousine_id")
    @Getter private Integer cousine_id;
}
