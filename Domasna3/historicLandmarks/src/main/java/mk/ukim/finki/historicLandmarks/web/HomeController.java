package mk.ukim.finki.historicLandmarks.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    private String getPage(Model model){
        model.addAttribute("bodyContent", "home");
        return "master-template";
    }
}
