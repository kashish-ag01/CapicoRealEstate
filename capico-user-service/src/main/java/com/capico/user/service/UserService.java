package com.capico.user.service;

import com.capico.user.dto.request.ChangePasswordRequest;
import com.capico.user.dto.request.CreateUserRequest;
import com.capico.user.dto.request.RegisterUserRequest;
import com.capico.user.dto.request.UpdateUserRequest;
import com.capico.user.dto.response.UserResponse;
import com.capico.user.entity.User;
import com.capico.user.enums.Role;
import com.capico.user.enums.UserStatus;
import com.capico.user.exception.DuplicateEmailException;
import com.capico.user.exception.DuplicateMobileNumberException;
import com.capico.user.exception.InvalidRequestException;
import com.capico.user.exception.UnauthorizedException;
import com.capico.user.exception.UserNotFoundException;
import com.capico.user.mapper.UserMapper;
import com.capico.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.capico.user.enums.UserActivityType;
import java.util.List;
import com.capico.user.enums.UserActivityType;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserActivityService userActivityService;
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            UserActivityService userActivityService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userActivityService = userActivityService;
    }

    /*
     * Public registration.
     * The client cannot choose role or status.
     */
    public UserResponse registerUser(
            RegisterUserRequest request
    ) {
        checkDuplicateEmail(request.getEmail());
        checkDuplicateMobileNumber(request.getMobileNumber());

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setMobileNumber(request.getMobileNumber());
        user.setGender(request.getGender());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setCountry(request.getCountry());

        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);
        userActivityService.record(
                savedUser,
                UserActivityType.USER_REGISTERED,
                "User registered successfully"
        );

        return userMapper.toResponse(savedUser);
    }

    /*
     * Administrator-only creation.
     * Protect the controller endpoint with ADMIN authorization.
     */
    public UserResponse createUser(
            CreateUserRequest request
    ) {
        checkDuplicateEmail(request.getEmail());
        checkDuplicateMobileNumber(
                request.getMobileNumber()
        );

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setMobileNumber(
                request.getMobileNumber()
        );

        user.setGender(request.getGender());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setCountry(request.getCountry());

        user.setRole(request.getRole());
        user.setStatus(request.getStatus());

        User savedUser =
                userRepository.save(user);

        userActivityService.record(
                savedUser,
                UserActivityType.USER_CREATED_BY_ADMIN,
                "User account created by administrator"
        );

        return userMapper.toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user = findUserById(userId);

        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = findUserByEmail(email);

        return userMapper.toResponse(user);
    }

    /*
     * The authenticated user updates only their own profile.
     * Role, status, email and password are not changed here.
     */
    public UserResponse updateCurrentUser(
            String authenticatedEmail,
            UpdateUserRequest request
    ) {
        User user = findUserByEmail(authenticatedEmail);

        boolean mobileAlreadyUsed =
                userRepository.existsByMobileNumberAndUserIdNot(
                        request.getMobileNumber(),
                        user.getUserId()
                );

        if (mobileAlreadyUsed) {
            throw new DuplicateMobileNumberException(
                    "Another user already exists with mobile number: "
                            + request.getMobileNumber()
            );
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMobileNumber(request.getMobileNumber());
        user.setGender(request.getGender());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setCountry(request.getCountry());

        User updatedUser = userRepository.save(user);

        userActivityService.record(
                updatedUser,
                UserActivityType.PROFILE_UPDATED,
                "User profile updated"
        );

        return userMapper.toResponse(updatedUser);
    }

    /*
     * Administrator-only deletion.
     */
    public void deleteUser(Long userId) {
        User user = findUserById(userId);

        userActivityService.record(
                user,
                UserActivityType.USER_DELETED,
                "User account deleted by administrator"
        );

        userRepository.delete(user);
    }

    public void changePassword(
            String authenticatedEmail,
            ChangePasswordRequest request
    ) {
        User user = findUserByEmail(authenticatedEmail);

        boolean currentPasswordMatches =
                passwordEncoder.matches(
                        request.getCurrentPassword(),
                        user.getPassword()
                );

        if (!currentPasswordMatches) {
            throw new UnauthorizedException(
                    "Current password is incorrect"
            );
        }

        if (!request.getNewPassword().equals(
                request.getConfirmPassword()
        )) {
            throw new InvalidRequestException(
                    "New password and confirm password do not match"
            );
        }

        boolean sameAsCurrentPassword =
                passwordEncoder.matches(
                        request.getNewPassword(),
                        user.getPassword()
                );

        if (sameAsCurrentPassword) {
            throw new InvalidRequestException(
                    "New password must be different from the current password"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);
        userActivityService.record(
                user,
                UserActivityType.PASSWORD_CHANGED,
                "User password changed"
        );
    }

    @Transactional(readOnly = true)
    public User login(
            String email,
            String plainPassword
    ) {
        User user = userRepository
                .findByEmailIgnoreCase(normalizeEmail(email))
                .orElseThrow(() ->
                        new UnauthorizedException(
                                "Invalid email or password"
                        )
                );

        if (user.getPassword() == null
                || !passwordEncoder.matches(
                plainPassword,
                user.getPassword()
        )) {
            throw new UnauthorizedException(
                    "Invalid email or password"
            );
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UnauthorizedException(
                    "User account is inactive"
            );
        }
        userActivityService.record(
                user,
                UserActivityType.LOGIN_SUCCESS,
                "User logged in successfully"
        );

        return user;
    }

    /*
     * Administrator changes another user's role or status.
     */
    public UserResponse updateUserAccess(
            Long userId,
            Role role,
            UserStatus status
    ) {
        User user = findUserById(userId);

        if (role == null) {
            throw new InvalidRequestException(
                    "Role is required"
            );
        }

        if (status == null) {
            throw new InvalidRequestException(
                    "Status is required"
            );
        }

        user.setRole(role);
        user.setStatus(status);

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with ID: " + userId
                        )
                );
    }

    private User findUserByEmail(String email) {
        return userRepository
                .findByEmailIgnoreCase(normalizeEmail(email))
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + email
                        )
                );
    }

    private void checkDuplicateEmail(String email) {
        String normalizedEmail = normalizeEmail(email);

        if (userRepository.existsByEmailIgnoreCase(
                normalizedEmail
        )) {
            throw new DuplicateEmailException(
                    "User already exists with email: "
                            + normalizedEmail
            );
        }
    }

    private void checkDuplicateMobileNumber(
            String mobileNumber
    ) {
        if (userRepository.existsByMobileNumber(
                mobileNumber
        )) {
            throw new DuplicateMobileNumberException(
                    "User already exists with mobile number: "
                            + mobileNumber
            );
        }
    }

    private String normalizeEmail(String email) {
        return email == null
                ? null
                : email.trim().toLowerCase();
    }
}