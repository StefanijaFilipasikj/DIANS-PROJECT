package mk.ukim.finki.historicLandmarks.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

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
//    @ManyToOne
//    private HistoricLandmark historicLandmark;
    private String comment;
    private Double rating;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

//    public Review(User user, HistoricLandmark historicLandmark, String comment, Double rating, LocalDateTime date) {
//        this.user = user;
//        this.historicLandmark = historicLandmark;
//        this.comment = comment;
//        this.rating = rating;
//        this.date = date;
//    }


    public Review(User user, String comment, Double rating, LocalDateTime date) {
        this.user = user;
        this.comment = comment;
        this.rating = rating;
        this.date = date;
    }
}
