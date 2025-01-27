package airlinereservation.project.Airlinereservation.config;

import airlinereservation.project.Airlinereservation.models.Role;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.models.enums.UserRole;
import airlinereservation.project.Airlinereservation.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataLoaderConfig {

    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("123456789"));
                admin.setEmail("admin@example.com");
                admin.setRoles(Set.of(new Role(UserRole.ROLE_ADMIN))); 
                userRepository.save(admin);
            }

            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setEmail("user@example.com");
                user.setRoles(Set.of(new Role(UserRole.ROLE_USER))); 
                userRepository.save(user);
            }
        };
    }
}
