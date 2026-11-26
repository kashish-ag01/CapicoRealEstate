package com.capico.user.controller;

import com.capico.user.dto.request.ChangePasswordRequest;
import com.capico.user.dto.request.CreateUserRequest;
import com.capico.user.dto.request.LoginRequest;
import com.capico.user.dto.request.RegisterUserRequest;
import com.capico.user.dto.request.UpdateUserRequest;
import com.capico.user.dto.response.LoginResponse;
import com.capico.user.dto.response.UserResponse;
import com.capico.user.entity.User;
import com.capico.user.security.JwtService;
import com.capico.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.capico.user.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import com.capico.user.dto.response.UserActivityResponse;
import com.capico.user.service.UserActivityService;
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final LogoutService logoutService;
    private final UserActivityService userActivityService;
    public UserController(
            UserService userService,
            JwtService jwtService,
            LogoutService logoutService,
            UserActivityService userActivityService
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.logoutService = logoutService;
        this.userActivityService = userActivityService;
    }

    /*
     * Public registration.
     * Role is always USER and status is always ACTIVE.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody RegisterUserRequest request
    ) {
        UserResponse registeredUser =
                userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registeredUser);
    }

    /*
     * Public login.
     */
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
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request
    ) {
        String authorizationHeader =
                request.getHeader("Authorization");

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bearer token is required");
        }

        String token = authorizationHeader
                .substring(7)
                .trim();

        logoutService.logout(
                token,
                getClientIpAddress(request)
        );

        return ResponseEntity.ok(
                "Logout successful"
        );
    }


    /*
     * Authenticated user retrieves their own profile.
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            Authentication authentication
    ) {
        UserResponse user =
                userService.getUserByEmail(
                        authentication.getName()
                );

        return ResponseEntity.ok(user);
    }
    @GetMapping("/admin/activities")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserActivityResponse>>
    getAllUserActivities() {

        return ResponseEntity.ok(
                userActivityService.getAllActivities()
        );
    }
    @GetMapping("/admin/{userId}/activities")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserActivityResponse>>
    getUserActivities(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                userActivityService.getActivitiesByUserId(userId)
        );
    }

    /*
     * Authenticated user updates their own profile.
     * This operation does not update email, password, role or status.
     */
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(
            @Valid @RequestBody UpdateUserRequest request,
            Authentication authentication
    ) {
        UserResponse updatedUser =
                userService.updateCurrentUser(
                        authentication.getName(),
                        request
                );

        return ResponseEntity.ok(updatedUser);
    }

    /*
     * Authenticated user changes their own password.
     */
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

    /*
     * Administrator creates a user.
     * Only this operation may accept role and status from the request.
     */
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUserByAdmin(
            @Valid @RequestBody CreateUserRequest request
    ) {
        UserResponse createdUser =
                userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    /*
     * Administrator retrieves all registered users.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users =
                userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    /*
     * Administrator retrieves one user by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id
    ) {
        UserResponse user =
                userService.getUserById(id);

        return ResponseEntity.ok(user);
    }

    /*
     * Administrator deletes a user.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
    private String getClientIpAddress(
            HttpServletRequest request
    ) {
        String forwardedAddress =
                request.getHeader("X-Forwarded-For");

        if (forwardedAddress != null
                && !forwardedAddress.isBlank()) {
            return forwardedAddress.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}