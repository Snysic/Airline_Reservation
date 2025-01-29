package airlinereservation.project.Airlinereservation.config;

import airlinereservation.project.Airlinereservation.models.Role;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.models.enums.UserRole;
import airlinereservation.project.Airlinereservation.repositories.RoleRepository;
import airlinereservation.project.Airlinereservation.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataLoaderConfig {

    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Role adminRole = roleRepository.findByName(UserRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(UserRole.ROLE_ADMIN)));

            Role userRole = roleRepository.findByName(UserRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(UserRole.ROLE_USER)));

            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("123456789"));
                admin.setEmail("admin@example.com");

                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(adminRole);
                admin.setRoles(adminRoles);

                userRepository.save(admin);
            }

            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setEmail("user@example.com");

                Set<Role> userRoles = new HashSet<>();
                userRoles.add(userRole);
                user.setRoles(userRoles);

                userRepository.save(user);
            }
        };
    }
}