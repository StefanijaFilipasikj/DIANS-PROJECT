package mk.ukim.finki.historicLandmarks.web;

import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/map")
public class MapController {
    private final HistoricLandmarkService historicLandmarkService;

    public MapController(HistoricLandmarkService historicLandmarkService) {
        this.historicLandmarkService = historicLandmarkService;
    }

    @GetMapping
    public String getPage(@RequestParam(required = false) String text,
                          @RequestParam(required = false) String region,
                          @RequestParam(required = false) String historicClass,
                          Model model){
        List<HistoricLandmark> landmarks = historicLandmarkService.findAll();
        if(text != null && !text.equals("")){
            landmarks = historicLandmarkService.searchByName(text);
        }
        if(region != null && !region.equals("")){
            landmarks = landmarks.stream().filter(h -> h.getRegion().equals(region)).toList();
        }
        if(historicClass != null && !historicClass.equals("")){
            landmarks = landmarks.stream().filter(h -> h.getHistoricClass()
                    .equals(historicLandmarkService.removeCapitalize(historicClass))).toList();
        }
        if(landmarks.isEmpty()){
            model.addAttribute("hasAny", false);
            landmarks = historicLandmarkService.findAll();
        }else{
            model.addAttribute("hasAny", true);
        }
        model.addAttribute("landmarks", landmarks);
        model.addAttribute("regions", historicLandmarkService.findAllRegions().stream());
        model.addAttribute("historicClasses", historicLandmarkService.findAllHistoricClass());
        model.addAttribute("bodyContent", "map-page");
        return "master-template";
    }
}
