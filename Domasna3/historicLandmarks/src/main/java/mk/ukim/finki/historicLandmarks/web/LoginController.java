package mk.ukim.finki.historicLandmarks.web;

import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.historicLandmarks.model.User;
import mk.ukim.finki.historicLandmarks.model.exception.InvalidArgumentsException;
import mk.ukim.finki.historicLandmarks.model.exception.InvalidUserCredentialsException;
import mk.ukim.finki.historicLandmarks.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String getLoginPage(Model model, @RequestParam(required = false) String error) {
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent", "login");
        return "master-template";
    }

    @PostMapping
    public String login(HttpServletRequest request, Model model) {
        User user = null;
        try {
            user = this.authService.login(request.getParameter("username"), request.getParameter("password"));
        } catch (Exception exception) {
            return "redirect:/login?error=" + exception.getMessage();
        }
        return "redirect:/home";
    }
}


