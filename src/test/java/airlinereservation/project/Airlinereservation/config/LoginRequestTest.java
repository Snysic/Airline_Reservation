package airlinereservation.project.Airlinereservation.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginRequestTest {

    @Test
    public void testDefaultConstructor() {
        LoginRequest loginRequest = new LoginRequest();

        assertNull(loginRequest.getUsername());
        assertNull(loginRequest.getPassword());
    }

    @Test
    public void testParameterizedConstructor() {
        String username = "testUser";
        String password = "testPassword";

        LoginRequest loginRequest = new LoginRequest(username, password);

        assertEquals(username, loginRequest.getUsername());
        assertEquals(password, loginRequest.getPassword());
    }

    @Test
    public void testSettersAndGetters() {
        LoginRequest loginRequest = new LoginRequest();

        String username = "newUser";
        String password = "newPassword";

        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        assertEquals(username, loginRequest.getUsername());
        assertEquals(password, loginRequest.getPassword());
    }

    @Test
    public void testToString() {
        String username = "user123";
        String password = "pass123";

        LoginRequest loginRequest = new LoginRequest(username, password);

        String expectedToString = "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';

        assertEquals(expectedToString, loginRequest.toString());
    }
}
