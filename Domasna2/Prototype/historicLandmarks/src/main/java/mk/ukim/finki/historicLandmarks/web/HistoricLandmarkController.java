package mk.ukim.finki.historicLandmarks.web;

import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "")
@Validated
@CrossOrigin(origins="*")
public class HistoricLandmarkController {

    private final HistoricLandmarkService historicLandmarkService;

    public HistoricLandmarkController(HistoricLandmarkService historicLandmarkService) {
        this.historicLandmarkService = historicLandmarkService;
    }

    @RequestMapping(path = "feedData")
    public void setDataInDB(){
        // commented to stop from changing the data
//        historicLandmarkService.saveData();
    }
}
