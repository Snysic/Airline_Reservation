package airlinereservation.project.Airlinereservation.config;

import airlinereservation.project.Airlinereservation.models.Role;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.models.enums.UserRole;
import airlinereservation.project.Airlinereservation.repositories.RoleRepository;
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
    private RoleRepository roleRepository;

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
        Role adminRole = new Role(UserRole.ROLE_ADMIN);
        when(roleRepository.findByName(UserRole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));

        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456789")).thenReturn("encodedAdminPassword");

        dataLoaderConfig.dataLoader(userRepository, roleRepository, passwordEncoder).run();

        verify(userRepository, times(1)).save(argThat(user ->
                user.getUsername().equals("admin") &&
                user.getPassword().equals("encodedAdminPassword") &&
                user.getEmail().equals("admin@example.com") &&
                user.getRoles().contains(adminRole)
        ));
    }

    @Test
    void testDataLoader_RegularUser() throws Exception {
        Role userRole = new Role(UserRole.ROLE_USER);
        when(roleRepository.findByName(UserRole.ROLE_USER)).thenReturn(Optional.of(userRole));

        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("user123")).thenReturn("encodedUserPassword");

        dataLoaderConfig.dataLoader(userRepository, roleRepository, passwordEncoder).run();

        verify(userRepository, times(1)).save(argThat(user ->
                user.getUsername().equals("user") &&
                user.getPassword().equals("encodedUserPassword") &&
                user.getEmail().equals("user@example.com") &&
                user.getRoles().contains(userRole)
        ));
    }

    @Test
    void testDataLoader_CreatesRolesIfNotExist() throws Exception {
        when(roleRepository.findByName(UserRole.ROLE_ADMIN)).thenReturn(Optional.empty());
        when(roleRepository.findByName(UserRole.ROLE_USER)).thenReturn(Optional.empty());

        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456789")).thenReturn("encodedAdminPassword");
        when(passwordEncoder.encode("user123")).thenReturn("encodedUserPassword");

        dataLoaderConfig.dataLoader(userRepository, roleRepository, passwordEncoder).run();

        verify(roleRepository, times(1)).save(argThat(role -> role.getName() == UserRole.ROLE_ADMIN));
        verify(roleRepository, times(1)).save(argThat(role -> role.getName() == UserRole.ROLE_USER));

        verify(userRepository, times(1)).save(argThat(user ->
                user.getUsername().equals("admin") &&
                user.getPassword().equals("encodedAdminPassword") &&
                user.getRoles().stream().anyMatch(role -> role.getName() == UserRole.ROLE_ADMIN)
        ));

        verify(userRepository, times(1)).save(argThat(user ->
                user.getUsername().equals("user") &&
                user.getPassword().equals("encodedUserPassword") &&
                user.getRoles().stream().anyMatch(role -> role.getName() == UserRole.ROLE_USER)
        ));
    }

    @Test
    void testDataLoader_NoDuplicateUsers() throws Exception {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(new User()));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(new User()));

        dataLoaderConfig.dataLoader(userRepository, roleRepository, passwordEncoder).run();

        verify(userRepository, never()).save(any(User.class));
    }
}