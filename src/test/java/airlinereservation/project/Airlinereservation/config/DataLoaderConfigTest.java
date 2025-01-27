package airlinereservation.project.Airlinereservation.config;

import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.models.enums.UserRole;
import airlinereservation.project.Airlinereservation.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

class DataLoaderConfigTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataLoaderConfig dataLoaderConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDataLoader_AdminUser() throws Exception {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456789")).thenReturn("encodedAdminPassword");

        dataLoaderConfig.dataLoader(userRepository, passwordEncoder).run();

        verify(userRepository, times(1)).save(argThat(user ->
                user.getUsername().equals("admin") &&
                user.getPassword().equals("encodedAdminPassword") &&
                user.getEmail().equals("admin@example.com") &&
                user.getRoles().stream().anyMatch(role -> role.getName() == UserRole.ROLE_ADMIN)
        ));
    }

    @Test
    void testDataLoader_RegularUser() throws Exception {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("user123")).thenReturn("encodedUserPassword");

        dataLoaderConfig.dataLoader(userRepository, passwordEncoder).run();

        verify(userRepository, times(1)).save(argThat(user ->
                user.getUsername().equals("user") &&
                user.getPassword().equals("encodedUserPassword") &&
                user.getEmail().equals("user@example.com") &&
                user.getRoles().stream().anyMatch(role -> role.getName() == UserRole.ROLE_USER)
        ));
    }

    @Test
    void testDataLoader_NoDuplicateUsers() throws Exception {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(new User()));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(new User()));

        dataLoaderConfig.dataLoader(userRepository, passwordEncoder).run();

        verify(userRepository, never()).save(any());
    }
}
