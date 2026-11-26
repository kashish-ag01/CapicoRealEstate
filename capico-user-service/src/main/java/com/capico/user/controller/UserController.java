package com.capico.user.controller;

import com.capico.user.dto.request.ChangePasswordRequest;
import com.capico.user.dto.request.CreateUserRequest;
import com.capico.user.dto.request.LoginRequest;
import com.capico.user.dto.request.UpdateUserRequest;
import com.capico.user.dto.response.LoginResponse;
import com.capico.user.dto.response.UserResponse;
import com.capico.user.entity.User;
import com.capico.user.security.JwtService;
import com.capico.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.capico.user.dto.response.UserSummaryResponse;
import com.capico.user.dto.response.UserSummaryResponse;
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(
            UserService userService,
            JwtService jwtService
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    // Public: create/register user
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        UserResponse savedUser = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
    }

    // Public: login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        User user = userService.login(
                request.getEmail(),
                request.getPassword()
        );

        String token = jwtService.generateToken(
                user.getUserId(),
                user.getEmail(),
                user.getRole().name()
        );

        LoginResponse response = new LoginResponse(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                token,
                "Bearer",
                user.getStatus(),
                "Login successful"
        );

        return ResponseEntity.ok(response);
    }

    // Logged-in user: get own profile
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            Authentication authentication
    ) {
        String loggedInEmail = authentication.getName();

        UserResponse user =
                userService.getUserByEmail(loggedInEmail);

        return ResponseEntity.ok(user);
    }

    // ADMIN only: get all users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ADMIN only: get any user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @GetMapping("/internal/{id}")
    public ResponseEntity<UserSummaryResponse> getInternalUser(
            @PathVariable Long id
    ) {
        UserSummaryResponse user =
                userService.getInternalUser(id);

        return ResponseEntity.ok(user);
    }

    // USER can update own account; ADMIN can update any account
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request,
            Authentication authentication
    ) {
        UserResponse updatedUser = userService.updateUser(
                id,
                request,
                authentication.getName(),
                authentication.getAuthorities()
        );

        return ResponseEntity.ok(updatedUser);
    }

    // ADMIN only: delete any user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id
    ) {
        userService.deleteUser(id);

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }

    // Logged-in user: change own password
    @PatchMapping("/me/password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {
        userService.changePassword(
                authentication.getName(),
                request
        );

        return ResponseEntity.ok(
                "Password changed successfully"
        );
    }
}