package mk.ukim.finki.landmark_microservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    private String comment;
    private Double rating;

    public Review(User user, String comment, Double rating) {
        this.user = user;
        this.comment = comment;
        this.rating = rating;
    }
}
