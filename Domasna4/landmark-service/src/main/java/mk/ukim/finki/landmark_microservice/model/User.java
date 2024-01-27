package mk.ukim.finki.landmark_microservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.landmark_microservice.model.enumerations.Role;

@Data
@Entity
@NoArgsConstructor
@Table(name = "App_Users")
public class User{
    @Id
    private String username;
    private String password;
    private String name;
    private String surname;
    private String photoUrl;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String password, String name, String surname, String photoUrl, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.photoUrl = photoUrl;
        this.role = role;
    }

    }
