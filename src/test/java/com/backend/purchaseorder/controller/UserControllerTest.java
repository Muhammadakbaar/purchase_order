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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    public void testGetAllUsers() throws Exception {
        List<UserDTO> users = Arrays.asList(new UserDTO(), new UserDTO());
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testGetUserById() throws Exception {
        UserDTO user = new UserDTO();
        when(userService.getUserById(anyInt())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());

        ResponseEntity<UserDTO> response = userController.getUserById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        when(userService.getUserById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());

        ResponseEntity<UserDTO> response = userController.getUserById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
public void testCreateUser() throws Exception {
    UserDTO user = new UserDTO();
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setEmail("test@example.com");
    user.setPhone("1234567890");
    user.setCreatedBy("admin");
    user.setUpdatedBy("admin");

    when(userService.createUser(any(UserDTO.class))).thenReturn(user);

    mockMvc.perform(post("/users")
            .contentType("application/json")
            .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"test@example.com\",\"phone\":\"1234567890\",\"createdBy\":\"admin\",\"updatedBy\":\"admin\"}"))
            .andExpect(status().isCreated());

    ResponseEntity<UserDTO> response = userController.createUser(user);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(user, response.getBody());
}
@Test
public void testUpdateUser() throws Exception {
    UserDTO user = new UserDTO();
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setEmail("updated@example.com");
    user.setPhone("1234567890");
    user.setCreatedBy("admin");
    user.setUpdatedBy("admin");

    when(userService.updateUser(anyInt(), any(UserDTO.class))).thenReturn(Optional.of(user));

    mockMvc.perform(put("/users/1")
            .contentType("application/json")
            .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"updated@example.com\",\"phone\":\"1234567890\",\"createdBy\":\"admin\",\"updatedBy\":\"admin\"}"))
            .andExpect(status().isOk());

    ResponseEntity<UserDTO> response = userController.updateUser(1, user);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(user, response.getBody());
}

@Test
public void testUpdateUserNotFound() throws Exception {
    when(userService.updateUser(anyInt(), any(UserDTO.class))).thenReturn(Optional.empty());

    mockMvc.perform(put("/users/1")
            .contentType("application/json")
            .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"updated@example.com\",\"phone\":\"1234567890\",\"createdBy\":\"admin\",\"updatedBy\":\"admin\"}"))
            .andExpect(status().isNotFound());

    ResponseEntity<UserDTO> response = userController.updateUser(1, new UserDTO());
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
}

    @Test
    public void testDeleteUser() throws Exception {
        when(userService.deleteUser(anyInt())).thenReturn(true);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        ResponseEntity<Void> response = userController.deleteUser(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        when(userService.deleteUser(anyInt())).thenReturn(false);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound());

        ResponseEntity<Void> response = userController.deleteUser(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
