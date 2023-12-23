package mk.ukim.finki.historicLandmarks.web;

import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/home", "/"})
public class HomeController {
    private final HistoricLandmarkService historicLandmarkService;

    public HomeController(HistoricLandmarkService historicLandmarkService) {
        this.historicLandmarkService = historicLandmarkService;
    }

    @GetMapping
    private String getPage(Model model){
        model.addAttribute("top10Landmarks", this.historicLandmarkService.findTop10());
        model.addAttribute("bodyContent", "home");
        return "master-template";
    }
}
