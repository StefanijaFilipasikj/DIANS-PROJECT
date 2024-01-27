package mk.ukim.finki.landmark_microservice.config;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.landmark_microservice.model.HistoricLandmark;
import mk.ukim.finki.landmark_microservice.repository.HistoricLandmarkRepository;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
public class DataInitializer {

    private final HistoricLandmarkRepository historicLandmarkRepository;

    public DataInitializer(HistoricLandmarkRepository historicLandmarkRepository) {
        this.historicLandmarkRepository = historicLandmarkRepository;
    }

    @PostConstruct
    public void initData() {
        if(this.isEmpty()){
            this.deleteAllData();
            this.addInitialData();
        }
    }

    public void addInitialData(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\sfili\\Documents\\GitHub\\DIANS-PROJECT\\Domasna4\\landmark-service\\src\\main\\resources\\filteredData.csv"));
            String line;
            br.readLine(); //skip header
            while ((line = br.readLine())!=null){
                String [] data = line.split(",",-1);
                if (data.length == 7){
                    HistoricLandmark hl = new HistoricLandmark();
                    hl.setLat(Double.parseDouble(data[0]));
                    hl.setLon(Double.parseDouble(data[1]));
                    hl.setHistoricClass(data[2]);
                    hl.setName(data[3]);
                    hl.setAddress(data[4]);
                    hl.setRegion(data[5]);
                    hl.setPhotoUrl(data[6]);
                    historicLandmarkRepository.save(hl);
                }
            }}
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteAllData() {
        this.historicLandmarkRepository.deleteAll();
    }

    public boolean isEmpty() {
        return this.historicLandmarkRepository.count() == 0;
    }
}
