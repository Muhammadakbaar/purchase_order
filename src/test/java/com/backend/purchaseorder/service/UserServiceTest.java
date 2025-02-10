package com.backend.purchaseorder.service;

import com.backend.purchaseorder.dto.user.UserDTO;
import com.backend.purchaseorder.entity.User;
import com.backend.purchaseorder.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDTOs() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId(1);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        users.add(user1);
        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@example.com");
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> userDTOS = userService.getAllUsers();

        assertEquals(2, userDTOS.size());
        assertEquals("John", userDTOS.get(0).getFirstName());
        assertEquals("Jane", userDTOS.get(1).getFirstName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUserDTO_WhenUserExists() {
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<UserDTO> userDTO = userService.getUserById(userId);

        assertTrue(userDTO.isPresent());
        assertEquals("John", userDTO.get().getFirstName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserById_ShouldReturnEmpty_WhenUserDoesNotExist() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<UserDTO> userDTO = userService.getUserById(userId);

        assertFalse(userDTO.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void createUser_ShouldReturnCreatedUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("john.doe@example.com");

        User user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setCreatedDatetime(LocalDateTime.now());

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals("John", createdUser.getFirstName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserDTO_WhenUserExists() {
        Integer userId = 1;
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Updated");
        userDTO.setLastName("User");
        userDTO.setEmail("updated.user@example.com");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setEmail("john.doe@example.com");
        existingUser.setCreatedDatetime(LocalDateTime.now());

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        updatedUser.setEmail("updated.user@example.com");
        updatedUser.setCreatedDatetime(existingUser.getCreatedDatetime());
        updatedUser.setUpdatedDatetime(LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        Optional<UserDTO> result = userService.updateUser(userId, userDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated", result.get().getFirstName());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ShouldReturnEmpty_WhenUserDoesNotExist() {
        Integer userId = 1;
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Updated");
        userDTO.setLastName("User");
        userDTO.setEmail("updated.user@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<UserDTO> result = userService.updateUser(userId, userDTO);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldReturnTrue_WhenUserExists() {
        Integer userId = 1;
        when(userRepository.existsById(userId)).thenReturn(true);

        boolean result = userService.deleteUser(userId);

        assertTrue(result);
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_ShouldReturnFalse_WhenUserDoesNotExist() {
        Integer userId = 1;
        when(userRepository.existsById(userId)).thenReturn(false);

        boolean result = userService.deleteUser(userId);

        assertFalse(result);
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(0)).deleteById(userId);
    }
}
