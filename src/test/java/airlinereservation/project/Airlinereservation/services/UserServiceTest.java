package airlinereservation.project.Airlinereservation.services;

import airlinereservation.project.Airlinereservation.models.Role;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.models.enums.UserRole;
import airlinereservation.project.Airlinereservation.repositories.ReservationRepository;
import airlinereservation.project.Airlinereservation.repositories.RoleRepository;
import airlinereservation.project.Airlinereservation.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        Role userRole = new Role();
        userRole.setName(UserRole.ROLE_USER);

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenAnswer(invocation -> "encodedPassword");
        when(roleRepository.findByName(UserRole.ROLE_USER)).thenAnswer(invocation -> Optional.of(userRole));
        when(userRepository.save(user)).thenAnswer(invocation -> {
            user.setId(1L);
            return user;
        });

        User result = userService.registerUser(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("encodedPassword", result.getPassword());
        assertTrue(result.getRoles().contains(userRole));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        User user = new User();
        user.setUsername("existinguser");

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));

        assertEquals("User already exists with username: existinguser", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testGetUserById_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(1L)).thenAnswer(invocation -> Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenAnswer(invocation -> Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserById(1L));

        assertEquals("User not found with ID: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testUploadProfilePicture_Success() throws Exception {
        User user = new User();
        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenAnswer(invocation -> new byte[]{1, 2, 3});

        userService.uploadProfilePicture(user, file);

        assertArrayEquals(new byte[]{1, 2, 3}, user.getProfilePicture());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUploadProfilePicture_EmptyFile() {
        User user = new User();
        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.uploadProfilePicture(user, file));

        assertEquals("File cannot be null or empty", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(1L));

        assertEquals("User not found with ID: 1", exception.getMessage());
        verify(userRepository, never()).deleteById(1L);
    }
}