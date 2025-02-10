package com.backend.purchaseorder.service;

import com.backend.purchaseorder.dto.user.UserDTO;
import com.backend.purchaseorder.entity.User;
import com.backend.purchaseorder.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Integer id) {
        log.info("Fetching user by id: {}", id);
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }

    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("UserDTO cannot be null");
        }
        log.info("Creating user with email: {}", userDTO.getEmail());
        User user = convertToEntity(userDTO);
        user.setCreatedDatetime(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public Optional<UserDTO> updateUser(Integer id, UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("UserDTO cannot be null");
        }
        log.info("Updating user with id: {}", id);
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(userDTO.getFirstName());
                    existingUser.setLastName(userDTO.getLastName());
                    existingUser.setEmail(userDTO.getEmail());
                    existingUser.setPhone(userDTO.getPhone());
                    existingUser.setUpdatedBy(userDTO.getUpdatedBy());
                    existingUser.setUpdatedDatetime(LocalDateTime.now());

                    User updatedUser = userRepository.save(existingUser);
                    return convertToDTO(updatedUser);
                });
    }

    public boolean deleteUser(Integer id) {
        log.info("Deleting user with id: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getCreatedBy(),
                user.getUpdatedBy()
        );
    }

    private User convertToEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .createdBy(userDTO.getCreatedBy())
                .updatedBy(userDTO.getUpdatedBy())
                .build();
    }
}