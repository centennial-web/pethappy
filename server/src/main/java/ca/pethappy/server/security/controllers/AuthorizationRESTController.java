package ca.pethappy.server.security.controllers;

import ca.pethappy.server.security.models.TokenUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthorizationRESTController {
    @GetMapping("/login")
    public String login(@AuthenticationPrincipal TokenUserDetails principal) {
        return principal.getToken();
    }
}