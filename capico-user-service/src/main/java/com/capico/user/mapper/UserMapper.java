package com.capico.user.mapper;

import com.capico.user.dto.response.UserResponse;
import com.capico.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {

        if (user == null) {
            return null;
        }

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
                user.getCreatedDate()
        );
    }
}