package com.capico.user.mapper;

import com.capico.user.dto.request.CreateUserRequest;
import com.capico.user.dto.request.UpdateUserRequest;
import com.capico.user.dto.response.UserResponse;
import com.capico.user.entity.User;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("all")
public class UserMapper {

    // Create request ko User entity mein convert karta hai
    public User toEntity(CreateUserRequest request) {

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setMobileNumber(request.getMobileNumber());
        user.setGender(request.getGender());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setCountry(request.getCountry());
        user.setRole(request.getRole());
        user.setStatus(request.getStatus());

        return user;
    }

    // Update request ki values existing entity mein copy karta hai
    public void updateEntity(
            UpdateUserRequest request,
            User existingUser) {

        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        existingUser.setEmail(request.getEmail());
        existingUser.setMobileNumber(request.getMobileNumber());
        existingUser.setGender(request.getGender());
        existingUser.setCity(request.getCity());
        existingUser.setState(request.getState());
        existingUser.setCountry(request.getCountry());
        existingUser.setRole(request.getRole());
        existingUser.setStatus(request.getStatus());

        /*
         Password yahan set nahi kar rahe.
         Password BCrypt encode karke UserService mein set hoga.
        */
    }

    // User entity ko safe response DTO mein convert karta hai
    public UserResponse toResponse(User user) {

        return new UserResponse(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getGender(),
                user.getCity(),
                user.getState(),
                user.getCountry(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}