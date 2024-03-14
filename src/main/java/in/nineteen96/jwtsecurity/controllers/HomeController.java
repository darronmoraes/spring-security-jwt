package in.nineteen96.jwtsecurity.controllers;

import in.nineteen96.jwtsecurity.models.User;
import in.nineteen96.jwtsecurity.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    @Autowired
    private UserService userService;

    /*  http://localhost:8001/home/users  */
    @GetMapping("/users")
    public List<User> getUser() {
        log.info("GET USER ENDPOINT");
        return userService.getUsers();
    }

    @GetMapping("/current")
    public String getLoggedInUser(Principal principal) {
        return principal.getName();
    }

}
