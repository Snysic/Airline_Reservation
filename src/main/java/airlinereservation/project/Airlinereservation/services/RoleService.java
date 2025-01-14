package airlinereservation.project.Airlinereservation.services;

import org.springframework.stereotype.Service;

import airlinereservation.project.Airlinereservation.models.Role;
import airlinereservation.project.Airlinereservation.repositories.RoleRepository;

import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found: " + name));
    }

    public Role createRole(String name) {
        Optional<Role> existingRole = roleRepository.findByName(name);
        if (existingRole.isPresent()) {
            throw new RuntimeException("Role already exists: " + name);
        }

        Role role = new Role(name);
        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with ID: " + id);
        }
        roleRepository.deleteById(id);
    }

    public boolean roleExists(String name) {
        return roleRepository.findByName(name).isPresent();
    }
}
