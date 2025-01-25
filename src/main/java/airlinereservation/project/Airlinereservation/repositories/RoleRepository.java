package airlinereservation.project.Airlinereservation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import airlinereservation.project.Airlinereservation.models.Role;
import airlinereservation.project.Airlinereservation.models.enums.UserRole;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name); 
}
