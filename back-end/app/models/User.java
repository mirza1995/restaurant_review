package models;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import play.data.validation.Constraints;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter @Setter private Integer id;

    @Constraints.Required
    @Column(name = "firstName")
    @Getter @Setter private String firstName;

    @Constraints.Required
    @Column(name = "lastName")
    @Getter @Setter private String lastName;

    @Constraints.Required
    @Column(name = "email")
    @Getter @Setter private String email;

    @Constraints.Required
    @Column(name = "phoneNumber")
    @Getter @Setter private String phoneNumber;

    @Constraints.Required
    @Column(name = "country")
    @Getter @Setter private String country;

    @Constraints.Required
    @Column(name = "city")
    @Getter @Setter private String city;

    @Constraints.Required
    @Column(name = "password")
    @Getter @Setter private String password;

    @Column(name = "account")
    @Getter @Setter private String account = "user";

    public User(@Constraints.Required String firstName, @Constraints.Required String lastName, @Constraints.Required String email, @Constraints.Required String phoneNumber, @Constraints.Required String country, @Constraints.Required String city, @Constraints.Required String password, String account) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.city = city;
        this.password = password;
        this.account = account;
    }

    public User() {
    }
}
