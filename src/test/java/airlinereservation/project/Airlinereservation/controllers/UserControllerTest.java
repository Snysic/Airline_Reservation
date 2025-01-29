package airlinereservation.project.Airlinereservation.controllers;

import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private User testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("securepassword");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetUserProfile() throws Exception {
        when(authentication.getName()).thenReturn("testuser");
        when(userService.getUserByUsername("testuser")).thenReturn(testUser);

        mockMvc.perform(get("/api/v1/user/profile")
                        .with(request -> {
                            request.setUserPrincipal(authentication);
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"));

        verify(userService, times(1)).getUserByUsername("testuser");
    }

    @Test
    void testGetUserReservations() throws Exception {
        List<String> reservations = List.of("Reservation 1", "Reservation 2");
        when(authentication.getName()).thenReturn("testuser");
        when(userService.getUserByUsername("testuser")).thenReturn(testUser);
        when(userService.getUserReservations(testUser)).thenReturn(reservations);

        mockMvc.perform(get("/api/v1/user/reservations")
                        .with(request -> {
                            request.setUserPrincipal(authentication);
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0]").value("Reservation 1"))
                .andExpect(jsonPath("$[1]").value("Reservation 2"));

        verify(userService, times(1)).getUserByUsername("testuser");
        verify(userService, times(1)).getUserReservations(testUser);
    }

    @Test
    void testUploadProfilePicture_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "profile.jpg", "image/jpeg", "mock image content".getBytes());

        when(authentication.getName()).thenReturn("testuser");
        when(userService.getUserByUsername("testuser")).thenReturn(testUser);
        doNothing().when(userService).uploadProfilePicture(any(User.class), any(MockMultipartFile.class));

        mockMvc.perform(multipart("/api/v1/user/profile/upload")
                        .file(file)
                        .with(request -> {
                            request.setUserPrincipal(authentication);
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().string("Profile picture uploaded successfully"));

        verify(userService, times(1)).getUserByUsername("testuser");
        verify(userService, times(1)).uploadProfilePicture(any(User.class), any(MockMultipartFile.class));
    }

    @Test
    void testUploadProfilePicture_Failure_EmptyFile() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("file", "", "image/jpeg", new byte[0]);

        mockMvc.perform(multipart("/api/v1/user/profile/upload")
                        .file(emptyFile)
                        .with(request -> {
                            request.setUserPrincipal(authentication);
                            return request;
                        }))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("File cannot be empty"));
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        when(userService.registerUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/v1/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "newuser",
                                  "password": "securepassword",
                                  "email": "newuser@example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    void testRegisterUser_Failure_IncompleteData() throws Exception {
        doThrow(new IllegalArgumentException("User data is incomplete"))
                .when(userService).registerUser(any(User.class));

        mockMvc.perform(post("/api/v1/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "incompleteuser"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User data is incomplete"));

        verify(userService, times(1)).registerUser(any(User.class));
    }
}