package mk.ukim.finki.historicLandmarks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Historic_Landmarks")
public class HistoricLandmark implements Comparable<HistoricLandmark>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double lat;
    private double lon;
    private String historicClass;
    private String name;
    private String address;
    private String region;
    @Lob //Large Object
    private String photoUrl;

    public HistoricLandmark(double lat, double lon, String historicClass, String name, String address, String region, String photoUrl) {
        this.lat = lat;
        this.lon = lon;
        this.historicClass = historicClass;
        this.name = name;
        this.address = address;
        this.region = region;
        this.photoUrl = photoUrl;
    }

    public HistoricLandmark() {
    }

    @Override
    public int compareTo(HistoricLandmark o) {
        return name.compareTo(o.getName());
    }
}