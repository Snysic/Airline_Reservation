package airlinereservation.project.Airlinereservation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<String>> getUserReservations(Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(userService.getUserReservations(user));
    }

    @PostMapping("/profile/upload")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file, Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());
        userService.uploadProfilePicture(user, file);
        return ResponseEntity.ok("Profile picture uploaded successfully");
    }
}
