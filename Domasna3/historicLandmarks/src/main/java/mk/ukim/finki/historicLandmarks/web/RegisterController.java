package mk.ukim.finki.historicLandmarks.web;


import mk.ukim.finki.historicLandmarks.model.exception.InvalidArgumentsException;
import mk.ukim.finki.historicLandmarks.model.exception.PasswordsDoNotMatchException;
import mk.ukim.finki.historicLandmarks.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final AuthService authService;

    public RegisterController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        model.addAttribute("bodyContent", "register");
        return "master-template";
    }

    @PostMapping
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           @RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam String photoUrl) {
        try{
            this.authService.register(username, password, repeatedPassword, name, surname, photoUrl);
            return "redirect:/login";
        } catch (InvalidArgumentsException | PasswordsDoNotMatchException exception) {
            return "redirect:/register?error=" + exception.getMessage();
        }
    }
}

