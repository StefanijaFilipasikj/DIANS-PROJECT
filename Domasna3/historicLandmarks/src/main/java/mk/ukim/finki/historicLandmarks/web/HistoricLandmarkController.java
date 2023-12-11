package mk.ukim.finki.historicLandmarks.web;

import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/database")
@Validated
@CrossOrigin(origins="*")
public class HistoricLandmarkController {

    private final HistoricLandmarkService historicLandmarkService;

    public HistoricLandmarkController(HistoricLandmarkService historicLandmarkService) {
        this.historicLandmarkService = historicLandmarkService;
    }

    @RequestMapping(path = "/feedData")
    public void setDataInDB(){
        //in case someone searches /database/feedData more than once we delete than load the database.
        //if you see this comment the next two lines so that there aren't any unnecessary deletes and loads.
        historicLandmarkService.deleteAllData();
        historicLandmarkService.saveData();
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<HistoricLandmark>> GetDatabase(){
        ResponseEntity<List<HistoricLandmark>> responseEntity = new ResponseEntity<>(historicLandmarkService.findAll(), HttpStatus.OK);
        return responseEntity;
    }
}
