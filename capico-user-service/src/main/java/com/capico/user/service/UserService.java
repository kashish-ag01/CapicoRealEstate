package com.capico.user.service;

import com.capico.user.entity.User;
import com.capico.user.enums.UserStatus;
import com.capico.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.capico.user.exception.UserNotFoundException;
import com.capico.user.exception.DuplicateEmailException;
import com.capico.user.exception.DuplicateMobileNumberException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.capico.user.dto.request.CreateUserRequest;
import com.capico.user.dto.response.UserResponse;
import com.capico.user.mapper.UserMapper;
import com.capico.user.dto.request.UpdateUserRequest;

import com.capico.user.dto.request.ChangePasswordRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import com.capico.user.dto.response.UserSummaryResponse;
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    // CREATE
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new DuplicateEmailException(
                    "User already exists with email: " + request.getEmail()
            );
        }

        if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new DuplicateMobileNumberException(
                    "User already exists with mobile number: "
                            + request.getMobileNumber()
            );
        }

        // DTO ko entity mein convert karta hai
        User user = userMapper.toEntity(request);

        // Plain password ko BCrypt hash mein convert karta hai
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(user);

        // Saved entity ko safe response DTO mein convert karta hai
        return userMapper.toResponse(savedUser);
    }

    // READ ALL
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    // READ BY ID
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with ID: " + id
                        )
                );

        return userMapper.toResponse(user);
    }// READ LOGGED-IN USER BY EMAIL
    public UserResponse getUserByEmail(String email) {

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + email
                        )
                );

        return userMapper.toResponse(user);
    }

    // UPDATE
    // UPDATE USER
// USER can update own account.
// ADMIN can update any account.
    public UserResponse updateUser(
            Long id,
            UpdateUserRequest request,
            String authenticatedEmail,
            Collection<? extends GrantedAuthority> authorities
    ) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with ID: " + id
                        )
                );

        User authenticatedUser = userRepository
                .findByEmailIgnoreCase(authenticatedEmail)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Authenticated user not found: "
                                        + authenticatedEmail
                        )
                );

        boolean isAdmin = authorities.stream()
                .anyMatch(authority ->
                        authority.getAuthority()
                                .equals("ROLE_ADMIN")
                );

        boolean isOwner = authenticatedUser
                .getUserId()
                .equals(id);

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException(
                    "You cannot update another user's account"
            );
        }

        // Check whether another user has the requested email
        if (userRepository.existsByEmailIgnoreCaseAndUserIdNot(
                request.getEmail(),
                id
        )) {
            throw new DuplicateEmailException(
                    "Another user already exists with email: "
                            + request.getEmail()
            );
        }

        // Check whether another user has the requested mobile number
        if (userRepository.existsByMobileNumberAndUserIdNot(
                request.getMobileNumber(),
                id
        )) {
            throw new DuplicateMobileNumberException(
                    "Another user already exists with mobile number: "
                            + request.getMobileNumber()
            );
        }

        userMapper.updateEntity(request, existingUser);

        /*
         * Password update yahan nahi karenge.
         * Password ke liye separate /me/password endpoint hai.
         */

        User savedUser = userRepository.save(existingUser);

        return userMapper.toResponse(savedUser);
    }

    // DELETE
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(
                    "User not found with ID: " + id
            );
        }

        userRepository.deleteById(id);
    }
    // LOGGED-IN USER CHANGES OWN PASSWORD
    public void changePassword(
            String authenticatedEmail,
            ChangePasswordRequest request
    ) {

        User user = userRepository
                .findByEmailIgnoreCase(authenticatedEmail)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: "
                                        + authenticatedEmail
                        )
                );

        // Verify current password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Current password is incorrect"
            );
        }

        // Verify confirmation
        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "New password and confirm password do not match"
            );
        }

        // New password must differ from current password
        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "New password must be different from current password"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);
    }
    public User login(String email, String password) {

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"
                ));

        if (user.getPassword() == null ||
                !passwordEncoder.matches(password, user.getPassword())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User account is inactive"
            );
        }

        return user;
    }
    public UserSummaryResponse getInternalUser(
            Long userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        UserSummaryResponse response =
                new UserSummaryResponse();

        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setRole(
                user.getRole().name()
        );
        response.setStatus(
                user.getStatus().name()
        );

        return response;
    }
    public UserSummaryResponse getInternalUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with ID: " + userId
                        )
                );

        return new UserSummaryResponse(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name(),
                user.getStatus().name()
        );
    }

}