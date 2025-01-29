package airlinereservation.project.Airlinereservation.services;

import airlinereservation.project.Airlinereservation.models.Role;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.models.enums.UserRole;
import airlinereservation.project.Airlinereservation.repositories.ReservationRepository;
import airlinereservation.project.Airlinereservation.repositories.RoleRepository;
import airlinereservation.project.Airlinereservation.repositories.UserRepository;
import airlinereservation.project.Airlinereservation.errors.NotFoundException;
import airlinereservation.project.Airlinereservation.errors.InvalidRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository,
                       ReservationRepository reservationRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.warn("Attempted to register an already existing username: {}", user.getUsername());
            throw new InvalidRequestException(400, "User already exists with username: " + user.getUsername());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(UserRole.ROLE_USER)
                .orElseThrow(() -> new NotFoundException(404, "Default role ROLE_USER not found"));

        user.setRoles(Set.of(userRole));
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with username: {}", user.getUsername());
        return savedUser;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(404, "User not found with ID: " + userId));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(404, "User not found with username: " + username));
    }

    public List<String> getUserReservations(User user) {
        if (user == null) {
            throw new InvalidRequestException(400, "User cannot be null");
        }

        return reservationRepository.findByUser(user)
                .stream()
                .map(reservation -> String.format(
                        "Reservation ID: %d, Flight: %s, Seats: %d, Status: %s",
                        reservation.getId(),
                        reservation.getFlight().getFlightCode(),
                        reservation.getReservedSeats(),
                        reservation.getStatus()
                ))
                .collect(Collectors.toList());
    }

    public void uploadProfilePicture(User user, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidRequestException(400, "File cannot be null or empty");
        }

        try {
            user.setProfilePicture(file.getBytes());
            userRepository.save(user);
            logger.info("Profile picture uploaded successfully for user: {}", user.getUsername());
        } catch (IOException e) {
            logger.error("Failed to upload profile picture for user: {}", user.getUsername(), e);
            throw new InvalidRequestException(500, "Failed to upload profile picture: " + e.getMessage());
        }
    }

    public User createUser(String username, String password, String email, byte[] profilePicture) {
        if (userRepository.existsByUsername(username)) {
            logger.warn("Attempted to create an already existing username: {}", username);
            throw new InvalidRequestException(400, "User already exists with username: " + username);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setProfilePicture(profilePicture);

        Role userRole = roleRepository.findByName(UserRole.ROLE_USER)
                .orElseThrow(() -> new NotFoundException(404, "Default role ROLE_USER not found"));

        user.setRoles(Set.of(userRole));
        User savedUser = userRepository.save(user);
        logger.info("User created successfully with username: {}", username);
        return savedUser;
    }

    public User updateUserRole(Long userId, UserRole newRole) {
        User user = getUserById(userId);
        Role role = roleRepository.findByName(newRole)
                .orElseThrow(() -> new NotFoundException(404, "Role not found: " + newRole));

        user.setRoles(Set.of(role));
        User updatedUser = userRepository.save(user);
        logger.info("Updated role for user: {} to {}", user.getUsername(), newRole);
        return updatedUser;
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existing user with ID: {}", id);
            throw new NotFoundException(404, "User not found with ID: " + id);
        }

        userRepository.deleteById(id);
        logger.info("User with ID {} was deleted successfully", id);
    }
}