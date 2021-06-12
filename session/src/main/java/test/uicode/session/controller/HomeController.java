package test.uicode.session.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public Object home(Authentication authentication) {
        return authentication.getPrincipal();
    }

    @PostMapping
    public String post() {
        return "POST";
    }

    @GetMapping("test")
    public String test() {
        return "test";
    }

    @GetMapping("developer")
    @Secured("ROLE_developer")
    public String admin() {
        return "developer";
    }

}
