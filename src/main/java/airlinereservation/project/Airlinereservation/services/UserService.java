package airlinereservation.project.Airlinereservation.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.repositories.RoleRepository;
import airlinereservation.project.Airlinereservation.repositories.UserRepository;
import airlinereservation.project.Airlinereservation.repositories.ReservationRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, 
                       ReservationRepository reservationRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public List<String> getUserReservations(User user) {
        return reservationRepository.findByUser(user).stream()
                .map(reservation -> "Reservation ID: " + reservation.getId() + 
                                    ", Flight: " + reservation.getFlight().getFlightCode() +
                                    ", Seats: " + reservation.getReservedSeats() +
                                    ", Status: " + reservation.getStatus())
                .collect(Collectors.toList());
    }

    public void uploadProfilePicture(User user, MultipartFile file) {
        try {
            user.setProfilePicture(file.getBytes());
            userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture");
        }
    }
}