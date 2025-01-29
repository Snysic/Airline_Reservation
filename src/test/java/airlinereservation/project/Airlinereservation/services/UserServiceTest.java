package airlinereservation.project.Airlinereservation.services;


import airlinereservation.project.Airlinereservation.errors.NotFoundException;
import airlinereservation.project.Airlinereservation.errors.InvalidRequestException;
import airlinereservation.project.Airlinereservation.models.Role;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.models.enums.UserRole;
import airlinereservation.project.Airlinereservation.repositories.ReservationRepository;
import airlinereservation.project.Airlinereservation.repositories.RoleRepository;
import airlinereservation.project.Airlinereservation.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private ReservationRepository reservationRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        roleRepository = mock(RoleRepository.class);

        userService = new UserService(userRepository, reservationRepository, passwordEncoder, roleRepository);
    }

    @Test
    void testRegisterUser_Success() {
        User user = new User("testUser", "password", "test@example.com");
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        Role role = new Role(UserRole.ROLE_USER);
        when(roleRepository.findByName(UserRole.ROLE_USER)).thenReturn(Optional.of(role));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User savedUser = userService.registerUser(user);

        assertNotNull(savedUser);
        verify(userRepository).save(Mockito.any(User.class));
        assertEquals("testUser", savedUser.getUsername());
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        User user = new User("testUser", "password", "test@example.com");
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> userService.registerUser(user));
        assertEquals("User already exists with username: testUser", exception.getMessage());
    }

    @Test
    void testGetUserById_UserExists() {
        User user = new User("testUser", "password", "test@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUserById(1L));
        assertEquals("User not found with ID: 1", exception.getMessage());
    }

    @Test
    void testGetUserReservations() {
        User user = new User("testUser", "password", "test@example.com");
        when(reservationRepository.findByUser(user)).thenReturn(List.of());

        List<String> reservations = userService.getUserReservations(user);

        assertNotNull(reservations);
        assertTrue(reservations.isEmpty());
    }

    @Test
    void testUploadProfilePicture_Success() throws IOException {
        User user = new User("testUser", "password", "test@example.com");
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn("file content".getBytes());

        userService.uploadProfilePicture(user, file);

        verify(userRepository).save(user);
        assertNotNull(user.getProfilePicture());
    }

    @Test
    void testUploadProfilePicture_FileIsEmpty() {
        User user = new User("testUser", "password", "test@example.com");
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> userService.uploadProfilePicture(user, file));
        assertEquals("File cannot be null or empty", exception.getMessage());
    }

    @Test
void testCreateUser_Success() {
    when(userRepository.existsByUsername("newUser")).thenReturn(false);
    
    when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
    
    Role role = new Role(UserRole.ROLE_USER);
    when(roleRepository.findByName(UserRole.ROLE_USER)).thenReturn(Optional.of(role));
    
    User expectedUser = new User("newUser", "encodedPassword", "new@example.com", null);
    expectedUser.setRoles(Set.of(role));

    when(userRepository.save(Mockito.any(User.class))).thenReturn(expectedUser);

    User actualUser = userService.createUser("newUser", "newPassword", "new@example.com", null);

    assertNotNull(actualUser);
    verify(userRepository).save(Mockito.any(User.class));
    
    assertEquals("newUser", actualUser.getUsername());
    assertEquals("encodedPassword", actualUser.getPassword());
    assertEquals("new@example.com", actualUser.getEmail());
    assertEquals(Set.of(role), actualUser.getRoles());
}

    @Test
    void testCreateUser_UserAlreadyExists() {
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                userService.createUser("existingUser", "password", "existing@example.com", null));

        assertEquals("User already exists with username: existingUser", exception.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.deleteUser(1L));
        assertEquals("User not found with ID: 1", exception.getMessage());
    }
}