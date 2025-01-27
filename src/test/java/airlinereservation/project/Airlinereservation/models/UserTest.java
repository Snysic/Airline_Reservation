package airlinereservation.project.Airlinereservation.models;

import org.junit.jupiter.api.Test;

import airlinereservation.project.Airlinereservation.models.enums.UserRole;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testDefaultConstructor() {
        User user = new User();

        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getEmail());
        assertNull(user.getProfilePicture());
        assertNull(user.getRoles());
        assertNull(user.getReservations());
    }

    @Test
    public void testParameterizedConstructorWithoutProfilePicture() {
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";

        User user = new User(username, password, email);

        assertNull(user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
        assertNull(user.getProfilePicture());
        assertNull(user.getRoles());
        assertNull(user.getReservations());
    }

    @Test
    public void testParameterizedConstructorWithProfilePicture() {
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        byte[] profilePicture = new byte[]{1, 2, 3, 4};

        User user = new User(username, password, email, profilePicture);

        assertNull(user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
        assertArrayEquals(profilePicture, user.getProfilePicture());
        assertNull(user.getRoles());
        assertNull(user.getReservations());
    }

    @Test
    public void testSettersAndGetters() {
        User user = new User();

        Long id = 1L;
        String username = "testUser";
        String password = "password123";
        String email = "test@example.com";
        byte[] profilePicture = new byte[]{1, 2, 3, 4};
        Set<Role> roles = new HashSet<>();
        Set<Reservation> reservations = new HashSet<>();

        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setProfilePicture(profilePicture);
        user.setRoles(roles);
        user.setReservations(reservations);

        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
        assertArrayEquals(profilePicture, user.getProfilePicture());
        assertEquals(roles, user.getRoles());
        assertEquals(reservations, user.getReservations());
    }

    @Test
    public void testIsAdmin() {
        Role adminRole = new Role(UserRole.ROLE_ADMIN);
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);

        User user = new User();
        user.setRoles(roles);

        assertTrue(user.isAdmin());
        assertFalse(user.isUser());
    }

    @Test
    public void testIsUser() {
        Role userRole = new Role(UserRole.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = new User();
        user.setRoles(roles);

        assertFalse(user.isAdmin());
        assertTrue(user.isUser());
    }

    @Test
    public void testToString() {
        String username = "testUser";
        String email = "test@example.com";
        Role adminRole = new Role(UserRole.ROLE_ADMIN);
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);

        User user = new User(username, "password123", email);
        user.setRoles(roles);

        String expectedToString = "User{" +
                "id=null, username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';

        assertEquals(expectedToString, user.toString());
    }
}
