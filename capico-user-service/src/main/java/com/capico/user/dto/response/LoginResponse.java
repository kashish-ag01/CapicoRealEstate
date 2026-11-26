package com.capico.user.dto.response;

import com.capico.user.enums.Role;
import com.capico.user.enums.UserStatus;

public class LoginResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private UserStatus status;
    private String message;
    private String token;
    private String tokenType;

    public LoginResponse(
            Long userId,
            String firstName,
            String lastName,
            String email,
            Role role,
            String token,
            String tokenType,
            UserStatus status,
            String message) {

        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.status = status;
        this.message = message;
        this.token = token;
        this.tokenType = tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

}