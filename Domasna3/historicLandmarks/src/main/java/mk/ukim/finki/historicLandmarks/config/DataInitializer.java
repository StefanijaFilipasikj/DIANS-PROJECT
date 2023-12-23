package mk.ukim.finki.historicLandmarks.config;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
import mk.ukim.finki.historicLandmarks.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final UserService userService;
    private final HistoricLandmarkService historicLandmarkService;

    public DataInitializer(UserService userService, HistoricLandmarkService historicLandmarkService) {
        this.userService = userService;
        this.historicLandmarkService = historicLandmarkService;
    }

    @PostConstruct
    public void initData() {
        if(this.historicLandmarkService.empty()){
            this.historicLandmarkService.deleteAllData();
            this.historicLandmarkService.saveData();
        }
        if(this.userService.empty()){
            this.userService.deleteAllData();
            this.userService.addInitialData();
        }
    }
}