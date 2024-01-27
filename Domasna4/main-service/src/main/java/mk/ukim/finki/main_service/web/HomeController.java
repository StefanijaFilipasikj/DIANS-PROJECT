package mk.ukim.finki.main_service.web;

import mk.ukim.finki.main_service.helper.RequestHelper;
import mk.ukim.finki.main_service.model.HistoricLandmark;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping({"/home", "/"})
public class HomeController {
    @GetMapping
    private String getPage(Model model){
        List<HistoricLandmark> landmarks = Arrays.asList(Objects.requireNonNull(RequestHelper.sendGetRequestForLandmarks(RequestHelper.createRequestUrl("/top-10-landmarks", new HashMap<>())).getBody()));
        model.addAttribute("top10Landmarks", landmarks);
        model.addAttribute("bodyContent", "home");
        return "master-template";
    }
}
