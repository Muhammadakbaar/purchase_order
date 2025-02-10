package com.backend.purchaseorder.controller;

import com.backend.purchaseorder.dto.user.UserDTO;
import com.backend.purchaseorder.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetAllUsers() {
        List<UserDTO> users = Arrays.asList(new UserDTO(), new UserDTO());
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testGetUserById() {
        UserDTO user = new UserDTO();
        when(userService.getUserById(anyInt())).thenReturn(Optional.of(user));

        ResponseEntity<UserDTO> response = userController.getUserById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUserByIdNotFound() {
        when(userService.getUserById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<UserDTO> response = userController.getUserById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateUser() {
        UserDTO user = new UserDTO();
        when(userService.createUser(any(UserDTO.class))).thenReturn(user);

        ResponseEntity<UserDTO> response = userController.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testUpdateUser() {
        UserDTO user = new UserDTO();
        when(userService.updateUser(anyInt(), any(UserDTO.class))).thenReturn(Optional.of(user));

        ResponseEntity<UserDTO> response = userController.updateUser(1, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testUpdateUserNotFound() {
        UserDTO user = new UserDTO();
        when(userService.updateUser(anyInt(), any(UserDTO.class))).thenReturn(Optional.empty());

        ResponseEntity<UserDTO> response = userController.updateUser(1, user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteUser() {
        when(userService.deleteUser(anyInt())).thenReturn(true);

        ResponseEntity<Void> response = userController.deleteUser(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() {
        when(userService.deleteUser(anyInt())).thenReturn(false);

        ResponseEntity<Void> response = userController.deleteUser(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}