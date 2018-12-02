package ca.pethappy.server.security.controllers;

import ca.pethappy.server.security.models.TokenUserDetails;
import ca.pethappy.server.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationRESTController {
    private final UsersService usersService;

    @Autowired
    public AuthorizationRESTController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/api/login")
    public String login(@AuthenticationPrincipal TokenUserDetails principal) {
        usersService.handle2FA(principal.getEmail());
        return principal.getToken();
    }
}
