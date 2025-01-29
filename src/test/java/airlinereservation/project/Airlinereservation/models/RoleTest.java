package airlinereservation.project.Airlinereservation.models;

import org.junit.jupiter.api.Test;

import airlinereservation.project.Airlinereservation.models.enums.UserRole;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    @Test
    public void testDefaultConstructor() {
        Role role = new Role();

        assertNull(role.getId());
        assertNull(role.getName());
        assertNull(role.getUsers());
    }

    @Test
    public void testParameterizedConstructor() {
        UserRole roleName = UserRole.ROLE_USER;
        Role role = new Role(roleName);

        assertNull(role.getId());
        assertEquals(roleName, role.getName());
        assertNull(role.getUsers());
    }

    @Test
    public void testSettersAndGetters() {
        Role role = new Role();

        Long id = 1L;
        UserRole roleName = UserRole.ROLE_USER;
        Set<User> users = new HashSet<>();

        role.setId(id);
        role.setName(roleName);
        role.setUsers(users);

        assertEquals(id, role.getId());
        assertEquals(roleName, role.getName());
        assertEquals(users, role.getUsers());
    }

    @Test
    public void testAddUsersToRole() {
        Role role = new Role(UserRole.ROLE_USER);

        User user1 = new User();
        user1.setUsername("User1");
        User user2 = new User();
        user2.setUsername("User2");

        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);

        role.setUsers(users);

        assertEquals(2, role.getUsers().size());
        assertTrue(role.getUsers().contains(user1));
        assertTrue(role.getUsers().contains(user2));
    }

}
