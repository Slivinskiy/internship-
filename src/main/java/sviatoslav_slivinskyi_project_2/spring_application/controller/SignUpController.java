package sviatoslav_slivinskyi_project_2.spring_application.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sviatoslav_slivinskyi_project_2.spring_application.model.User;
import sviatoslav_slivinskyi_project_2.spring_application.service.UserService;



@Controller
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    UserService userService;

    private static final Logger LOGGER = LogManager.getLogger(SignUpController.class);

    @GetMapping
    public String signupView(User user, Model model){
        model.addAttribute("user", user);
        return "signup";
    }

    @PostMapping
    public String signupUser(User user, Model model){
        String signupError = null;

        if (userService.getUserByName(user.getUsername()) != null){
            signupError = "Sorry, but this username already exists, choose another one.";
            LOGGER.info("username already exists");
        }
        if (signupError == null){
            User savedUser = userService.insertUser(user);
            if (savedUser == null){
                signupError = "There was an error signing you up. Please try again.";
                LOGGER.error("Problem with SignUp");
            }
        }
        if (signupError == null){
            model.addAttribute("signupSuccess", true);
            LOGGER.info("Successful SignUp");
            return "login";
        } else {
            model.addAttribute("signupError", signupError);
            LOGGER.error("Problem with SignUp");
        }
        return "signup";
    }


}
