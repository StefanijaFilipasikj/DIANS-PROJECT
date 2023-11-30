package mk.ukim.finki.historicLandmarks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Historic_Landmarks")
@NoArgsConstructor
@AllArgsConstructor
public class HistoricLandmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double lat;
    private double lon;
    private String historicClass;
    private String name;
    private String address;
    private String Region;
}