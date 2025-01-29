package airlinereservation.project.Airlinereservation.models;

import airlinereservation.project.Airlinereservation.models.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Role adminRole;
    private Role userRole;

    @BeforeEach
    void setUp() {
        adminRole = new Role();
        adminRole.setName(UserRole.ROLE_ADMIN);

        userRole = new Role();
        userRole.setName(UserRole.ROLE_USER);

        user = new User("testUser", "password", "testUser@example.com");
    }

    @Test
    void testIsAdmin_WhenUserHasAdminRole_ReturnsTrue() {
        user.setRoles(Collections.singleton(adminRole));

        assertTrue(user.isAdmin());
    }

    @Test
    void testIsAdmin_WhenUserDoesNotHaveAdminRole_ReturnsFalse() {
        user.setRoles(Collections.singleton(userRole));

        assertFalse(user.isAdmin());
    }

    @Test
    void testIsUser_WhenUserHasUserRole_ReturnsTrue() {
        user.setRoles(Collections.singleton(userRole));

        assertTrue(user.isUser());
    }

    @Test
    void testIsUser_WhenUserDoesNotHaveUserRole_ReturnsFalse() {
        user.setRoles(Collections.singleton(adminRole));

        assertFalse(user.isUser());
    }

    @Test
    void testSetAndGetProfilePicture() {
        byte[] picture = "dummyPictureData".getBytes();
        user.setProfilePicture(picture);

        assertArrayEquals(picture, user.getProfilePicture());
    }

    @Test
    void testToString() {
        user.setRoles(new HashSet<>(Set.of(userRole)));

        String userString = user.toString();
        assertTrue(userString.contains("testUser"));
        assertTrue(userString.contains("testUser@example.com"));
    }

    @Test
    void testGetAndSetId() {
        user.setId(123L);

        assertEquals(123L, user.getId());
    }

    @Test
    void testGetAndSetUsername() {
        user.setUsername("newUsername");

        assertEquals("newUsername", user.getUsername());
    }

    @Test
    void testGetAndSetPassword() {
        user.setPassword("newPassword");

        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void testGetAndSetEmail() {
        user.setEmail("newEmail@example.com");

        assertEquals("newEmail@example.com", user.getEmail());
    }

    @Test
    void testGetAndSetRoles() {
        Set<Role> roles = new HashSet<>(Set.of(adminRole, userRole));
        user.setRoles(roles);

        assertEquals(roles, user.getRoles());
    }

    @Test
    void testGetAndSetReservations() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);

        Set<Reservation> reservations = new HashSet<>();
        reservations.add(reservation);

        user.setReservations(reservations);

        assertEquals(reservations, user.getReservations());
    }
}
