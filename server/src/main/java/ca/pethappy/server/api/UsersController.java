package ca.pethappy.server.api;

import ca.pethappy.server.forms.UserRegistration;
import ca.pethappy.server.models.User;
import ca.pethappy.server.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UsersController {
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistration userRegistration, BindingResult result) {
        User user = UserRegistration.toUser(userRegistration);

        // Check if user exists
        if (usersService.findByEmail(user.getEmail()) != null) {
            result.rejectValue("email", null, "Email already exists");
        }

        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/api/users/{userId}/verificationCode")
    public ResponseEntity<?> userVerificationCode(@PathVariable Long userId) {
        try {
            String verificationCode = usersService.findById(userId).getVerificationCode();
            return new ResponseEntity<>(verificationCode, HttpStatus.OK);
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
