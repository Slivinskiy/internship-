package sviatoslav_slivinskyi_project_2.spring_application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sviatoslav_slivinskyi_project_2.spring_application.model.Credential;
import sviatoslav_slivinskyi_project_2.spring_application.model.User;
import sviatoslav_slivinskyi_project_2.spring_application.service.CredentialService;
import sviatoslav_slivinskyi_project_2.spring_application.service.EncryptionService;
import sviatoslav_slivinskyi_project_2.spring_application.service.UserService;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;


@Controller
@RequestMapping("/credentials")
public class CredentialController {

    @Autowired
    CredentialService credentialService;
    @Autowired
    EncryptionService encryptionService;
    @Autowired
    UserService userService;

    private static final int MIN_SQL_RESULT = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialController.class);

    @GetMapping
    public String viewCredentials(Model model, Authentication authentication){
        String username = authentication.getName();
        List<Credential> credentials = credentialService.getAllUserCredentials(username);
        model.addAttribute("credentials", credentials);
        return "credentials";
    }

    @GetMapping("/newCredentials")
    public String showCredentialsForm(@ModelAttribute("credentials") Credential credentials){
        return "credentials-form";
    }

    @GetMapping("/updateCredentials/{credentialId}")
    public String updateCredentialsForm(Credential credential, Model model){
        credential = credentialService.getCredentialsById(credential.getCredentialId());
        String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getSecretKey());
        credential.setPassword(decryptedPassword);
        model.addAttribute("credentialsId",credential.getCredentialId());
        model.addAttribute("credentials", credential);
        return "credentials-form";
    }

    @GetMapping("/deleteCredentials/{credentialId}")
    public String deleteCredentials(@ModelAttribute("credentials") Credential credentials, Model model){
        credentialService.deleteCredentials(credentials.getCredentialId());
        if (credentialService.getOptionalCredentials(credentials.getCredentialId()).isPresent()){
            model.addAttribute("ErrorDeleteCredentials", true);
            LOGGER.error("An error occurs during deleting credentials");
        } else {
            model.addAttribute("SuccessDeleteCredentials", true);
            LOGGER.info("Credentials were deleted");
        }
        return "result";
    }

    @PostMapping("/updateCredentials/{credentialId}")
    public String updateCredentials(@ModelAttribute("credentials") Credential credential, Model model){
        Credential credential1 = credentialService.getCredentialsById(credential.getCredentialId());
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential1.getSecretKey());
        credential1.setPassword(encryptedPassword);
        credential1.setUrl(credential.getUrl());
        credential1.setUsername(credential.getUsername());
        int result = credentialService.updateCredentials(credential1);
        if (result < MIN_SQL_RESULT){
            model.addAttribute("ErrorUpdateCredentials", true);
            LOGGER.error("An error occurs during updating credentials");
        } else {
            model.addAttribute("SuccessUpdateCredentials", true);
            LOGGER.info("Credentials were updated");
        }
        return "result";
    }

    @PostMapping("/newCredentials")
    public String addCredentials(@ModelAttribute("credentials") Credential credential, Model model, Authentication authentication){
        String username = authentication.getName();
        User user = userService.getUserByName(username);
        credential.setUser(user);
        if(credentialService.saveCredentials(credential) != null){
            model.addAttribute("credentialsSuccess", true);
            LOGGER.info("Credentials were saved");
        } else {
            model.addAttribute("credentialsError", true);
            LOGGER.error("An error occurs during deleting credentials");
        }
        return "result";
    }
}
