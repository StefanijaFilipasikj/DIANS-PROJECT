package mk.ukim.finki.historicLandmarks.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    public HistoricLandmark(Double lat, Double lon, String historicClass, String name, String address, String region, String photoUrl) {
        this.lat = lat;
        this.lon = lon;
        this.historicClass = historicClass;
        this.name = name;
        this.address = address;
        this.region = region;
        this.photoUrl = photoUrl;
        this.reviews = new ArrayList<>();
    }

    public Double getRating(){
        return reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
    }
    public Integer getNumberOfReviews(){
        return reviews.size();
    }
}