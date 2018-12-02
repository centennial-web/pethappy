package ca.pethappy.server.api;

import ca.pethappy.server.forms.UserSettings;
import ca.pethappy.server.models.User;
import ca.pethappy.server.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SettingsController {
    private final UsersService usersService;

    @Autowired
    public SettingsController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/api/settings/{userId}")
    public ResponseEntity<?> userSettings(@PathVariable Long userId) {
        try {
            return new ResponseEntity<>(usersService.getUserSettings(userId), HttpStatus.OK);
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/api/settings")
    public ResponseEntity<?> updateSettings(@RequestBody UserSettings userSettings) {
        try {
            usersService.updateUserSettings(userSettings);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
