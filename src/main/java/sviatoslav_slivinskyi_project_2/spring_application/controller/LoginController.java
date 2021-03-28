package sviatoslav_slivinskyi_project_2.spring_application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sviatoslav_slivinskyi_project_2.spring_application.model.User;


@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String viewLogin( @ModelAttribute("user") User user, Model model) {
        return "login";
    }

    @PostMapping()
    public String loginUser(@ModelAttribute User user, Model model) {
        return "login";
    }
}
