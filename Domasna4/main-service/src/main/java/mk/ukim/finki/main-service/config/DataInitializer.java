package mk.ukim.finki.historicLandmarks.config;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.historicLandmarks.service.DataService;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    private final DataService dataService;

    public DataInitializer(DataService dataService) {
        this.dataService = dataService;
    }

    @PostConstruct
    public void initData() {
        if(this.dataService.isEmpty()){
            this.dataService.deleteAllData();
            this.dataService.addInitialData();
        }
    }
}