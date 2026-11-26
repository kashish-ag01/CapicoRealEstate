package com.capico.user.service;

import com.capico.user.dto.response.UserActivityResponse;
import com.capico.user.entity.User;
import com.capico.user.entity.UserActivity;
import com.capico.user.enums.UserActivityType;
import com.capico.user.repository.UserActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserActivityService {

    private final UserActivityRepository userActivityRepository;

    public UserActivityService(
            UserActivityRepository userActivityRepository
    ) {
        this.userActivityRepository = userActivityRepository;
    }

    public void record(
            User user,
            UserActivityType activityType,
            String description
    ) {
        if (user == null) {
            return;
        }

        saveActivity(
                user.getUserId(),
                user.getEmail(),
                activityType,
                description,
                null
        );
    }

    public void recordLogout(
            Long userId,
            String email,
            String ipAddress
    ) {
        saveActivity(
                userId,
                email,
                UserActivityType.LOGOUT,
                "User logged out",
                ipAddress
        );
    }

    @Transactional(readOnly = true)
    public List<UserActivityResponse> getAllActivities() {
        return userActivityRepository
                .findAllByOrderByActivityDateDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserActivityResponse> getActivitiesByUserId(
            Long userId
    ) {
        return userActivityRepository
                .findByUserIdOrderByActivityDateDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void saveActivity(
            Long userId,
            String email,
            UserActivityType activityType,
            String description,
            String ipAddress
    ) {
        UserActivity activity = new UserActivity();

        activity.setUserId(userId);
        activity.setUserEmail(email);
        activity.setActivityType(activityType);
        activity.setDescription(description);
        activity.setIpAddress(ipAddress);

        userActivityRepository.save(activity);
    }

    private UserActivityResponse toResponse(
            UserActivity activity
    ) {
        return new UserActivityResponse(
                activity.getActivityId(),
                activity.getUserId(),
                activity.getUserEmail(),
                activity.getActivityType(),
                activity.getDescription(),
                activity.getIpAddress(),
                activity.getActivityDate()
        );
    }
}