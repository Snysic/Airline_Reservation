package airlinereservation.project.Airlinereservation.controllers;

import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<String>> getUserReservations(Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(userService.getUserReservations(user));
    }

    @PostMapping("/profile/upload")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file, Authentication authentication) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File cannot be empty");
        }

        User user = userService.getUserByUsername(authentication.getName());
        userService.uploadProfilePicture(user, file);
        return ResponseEntity.ok("Profile picture uploaded successfully");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User data is incomplete");
        }

        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

}
