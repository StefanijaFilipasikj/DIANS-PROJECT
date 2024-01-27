package mk.ukim.finki.main_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Historic_Landmarks")
public class HistoricLandmark{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double lat;
    private Double lon;
    private String historicClass;
    private String name;
    private String address;
    private String region;
    @Lob
    private String photoUrl;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Review> reviews;

    public Double getRating(){
        return reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
    }
    public Integer getNumberOfReviews(){
        return reviews.size();
    }
}