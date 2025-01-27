package airlinereservation.project.Airlinereservation.controllers;

import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("user", "password", "user@example.com");
        user.setId(1L);
    }

    @Test
    void testGetUserReservations() {
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(user);
        List<String> reservations = Arrays.asList("Reservation 1", "Reservation 2");
        when(userService.getUserReservations(user)).thenReturn(reservations);

        ResponseEntity<List<String>> response = userController.getUserReservations(authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(reservations);
        verify(userService, times(1)).getUserByUsername("user");
        verify(userService, times(1)).getUserReservations(user);
    }

    @Test
    void testUploadProfilePicture() {
        MockMultipartFile file = new MockMultipartFile("file", "profile.jpg", "image/jpeg", "test image".getBytes());
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(user);

        ResponseEntity<String> response = userController.uploadProfilePicture(file, authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Profile picture uploaded successfully");
        verify(userService, times(1)).getUserByUsername("user");
        verify(userService, times(1)).uploadProfilePicture(user, file);
    }

    @Test
    void testUploadProfilePictureEmptyFile() {
        MockMultipartFile file = new MockMultipartFile("file", "", "image/jpeg", new byte[0]);

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userController.uploadProfilePicture(file, authentication)
        );

        assertThat(exception.getMessage()).isEqualTo("File cannot be empty");
        verifyNoInteractions(userService);
    }

    @Test
    void testRegisterUser() {
        when(userService.registerUser(user)).thenReturn(user);

        ResponseEntity<String> response = userController.registerUser(user);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("User registered successfully");
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    void testRegisterUserWithIncompleteData() {
        User incompleteUser = new User();
        incompleteUser.setUsername("user");

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userController.registerUser(incompleteUser)
        );

        assertThat(exception.getMessage()).isEqualTo("User data is incomplete");
        verifyNoInteractions(userService);
    }
}