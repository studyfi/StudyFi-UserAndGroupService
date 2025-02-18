package com.studyfi.userandgroup.user.controller;

import com.studyfi.userandgroup.user.dto.UserDTO;
import com.studyfi.userandgroup.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register a new user
    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    // Get all users
    @GetMapping("/getusers")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get User by ID
    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    // Update user profile
    @PutMapping("/profile/{userId}")
    public UserDTO updateProfile(@PathVariable Integer userId, @RequestBody UserDTO userDTO) {  // Changed Long to Integer
        return userService.updateUserProfile(userId, userDTO);
    }

    // Add user to group
    @PostMapping("/addToGroup")
    public void addUserToGroup(@RequestParam Integer userId, @RequestParam Integer groupId) {  // Changed Long to Integer
        userService.addUserToGroup(userId, groupId);
    }
}