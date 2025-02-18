package com.studyfi.userandgroup.user.service;

import com.studyfi.userandgroup.group.model.Group;
import com.studyfi.userandgroup.user.dto.UserDTO;
import com.studyfi.userandgroup.user.model.User;
import com.studyfi.userandgroup.user.repo.UserRepo;
import com.studyfi.userandgroup.group.repo.GroupRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final GroupRepo groupRepo;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, GroupRepo groupRepo, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user
    public UserDTO registerUser(UserDTO userDTO) {
        // Validate password for registration
        validatePassword(userDTO.getPassword());

        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));  // Encrypt password during registration
        userRepo.save(user);
        return modelMapper.map(user, UserDTO.class);
    }

    // Get all users
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
    }

    // Get user by ID
    public UserDTO getUserById(Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(user, UserDTO.class);
    }

    // Update user profile
    public UserDTO updateUserProfile(Integer userId, UserDTO userDTO) {
        // Fetch the existing user from the database
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Update the user fields with new data
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        // Validate the new password before updating it
        validatePassword(userDTO.getPassword());
        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        user.setPhoneContact(userDTO.getPhoneContact());
        user.setBirthDate(userDTO.getBirthDate());
        user.setCountry(userDTO.getCountry());
        user.setAboutMe(userDTO.getAboutMe());
        user.setCurrentAddress(userDTO.getCurrentAddress());

        // Save the updated user to the repository
        userRepo.save(user);
        return modelMapper.map(user, UserDTO.class);
    }

    // Add user to a group
    public void addUserToGroup(Integer userId, Integer groupId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepo.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

        user.getGroups().add(group);
        group.getUsers().add(user);

        userRepo.save(user);
        groupRepo.save(group);
    }

    // Password validation logic
    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // Check minimum length (e.g., 8 characters)
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        // Check if the password contains at least one number
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }

        // Check if the password contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }

        // Check if the password contains at least one special character
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character");
        }
    }
}
