package airlinereservation.project.Airlinereservation.services;

import airlinereservation.project.Airlinereservation.models.Role;
import airlinereservation.project.Airlinereservation.models.enums.UserRole;
import airlinereservation.project.Airlinereservation.repositories.RoleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRoles() {
        Role role1 = new Role(UserRole.ROLE_ADMIN);
        Role role2 = new Role(UserRole.ROLE_USER);

        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        var roles = roleService.getAllRoles();

        assertEquals(2, roles.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    public void testGetRoleByName_Success() {
        Role role = new Role(UserRole.ROLE_ADMIN);

        when(roleRepository.findByName(UserRole.ROLE_ADMIN)).thenReturn(Optional.of(role));

        Role result = roleService.getRoleByName(UserRole.ROLE_ADMIN);

        assertNotNull(result);
        assertEquals(UserRole.ROLE_ADMIN, result.getName());
        verify(roleRepository, times(1)).findByName(UserRole.ROLE_ADMIN);
    }

    @Test
    public void testGetRoleByName_NotFound() {
        when(roleRepository.findByName(UserRole.ROLE_ADMIN)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roleService.getRoleByName(UserRole.ROLE_ADMIN);
        });

        assertEquals("Role not found: ROLE_ADMIN", exception.getMessage());
        verify(roleRepository, times(1)).findByName(UserRole.ROLE_ADMIN);
    }

    @Test
    public void testCreateRole_Success() {
        Role role = new Role(UserRole.ROLE_ADMIN);

        when(roleRepository.findByName(UserRole.ROLE_ADMIN)).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Role result = roleService.createRole(UserRole.ROLE_ADMIN);

        assertNotNull(result);
        assertEquals(UserRole.ROLE_ADMIN, result.getName());
        verify(roleRepository, times(1)).findByName(UserRole.ROLE_ADMIN);
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    public void testCreateRole_AlreadyExists() {
        Role role = new Role(UserRole.ROLE_ADMIN);

        when(roleRepository.findByName(UserRole.ROLE_ADMIN)).thenReturn(Optional.of(role));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roleService.createRole(UserRole.ROLE_ADMIN);
        });

        assertEquals("Role already exists: ROLE_ADMIN", exception.getMessage());
        verify(roleRepository, times(1)).findByName(UserRole.ROLE_ADMIN);
        verify(roleRepository, times(0)).save(any(Role.class));
    }

    @Test
    public void testDeleteRole_Success() {
        when(roleRepository.existsById(1L)).thenReturn(true);

        roleService.deleteRole(1L);

        verify(roleRepository, times(1)).existsById(1L);
        verify(roleRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteRole_NotFound() {
        when(roleRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roleService.deleteRole(1L);
        });

        assertEquals("Role not found with ID: 1", exception.getMessage());
        verify(roleRepository, times(1)).existsById(1L);
        verify(roleRepository, times(0)).deleteById(1L);
    }

    @Test
    public void testRoleExists() {
        when(roleRepository.findByName(UserRole.ROLE_ADMIN)).thenReturn(Optional.of(new Role(UserRole.ROLE_ADMIN)));

        boolean exists = roleService.roleExists(UserRole.ROLE_ADMIN);

        assertTrue(exists);
        verify(roleRepository, times(1)).findByName(UserRole.ROLE_ADMIN);
    }

    @Test
    public void testUpdateRole_Success() {
        Role role = new Role(UserRole.ROLE_USER);
        role.setId(1L);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.findByName(UserRole.ROLE_ADMIN)).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Role updatedRole = roleService.updateRole(1L, UserRole.ROLE_ADMIN);

        assertEquals(UserRole.ROLE_ADMIN, updatedRole.getName());
        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).findByName(UserRole.ROLE_ADMIN);
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    public void testUpdateRole_RoleNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roleService.updateRole(1L, UserRole.ROLE_ADMIN);
        });

        assertEquals("Role not found with ID: 1", exception.getMessage());
        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(0)).findByName(UserRole.ROLE_ADMIN);
        verify(roleRepository, times(0)).save(any(Role.class));
    }

}
