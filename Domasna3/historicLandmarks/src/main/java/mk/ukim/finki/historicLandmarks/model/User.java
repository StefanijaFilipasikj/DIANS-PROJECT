package mk.ukim.finki.historicLandmarks.model;

import jakarta.persistence.*;
import lombok.Data;
import mk.ukim.finki.historicLandmarks.model.enumerations.UserRoles;

@Data
@Entity
@Table(name = "App_Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    @Enumerated(EnumType.STRING)
    private UserRoles role;

    public User(String username, String password, String name, String surname) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = UserRoles.USER;
    }

    public User() {
    }
}
