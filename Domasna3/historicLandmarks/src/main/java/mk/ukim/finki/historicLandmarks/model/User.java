package mk.ukim.finki.historicLandmarks.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.historicLandmarks.model.enumerations.UserRoles;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "App_Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String photoUrl;
    @Enumerated(EnumType.STRING)
    private UserRoles role;
//    @OneToMany(fetch = FetchType.EAGER)
//    private List<Review> reviews;

    public User(String username, String password, String name, String surname, String photoUrl) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.photoUrl = photoUrl;
        this.role = UserRoles.USER;
//        this.reviews = new ArrayList<>();
    }
}
