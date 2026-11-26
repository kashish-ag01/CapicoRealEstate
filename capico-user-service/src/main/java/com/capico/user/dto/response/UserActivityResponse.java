package com.capico.user.dto.response;

import com.capico.user.enums.UserActivityType;

import java.time.LocalDateTime;

public class UserActivityResponse {

    private Long activityId;
    private Long userId;
    private String userEmail;
    private UserActivityType activityType;
    private String description;
    private String ipAddress;
    private LocalDateTime activityDate;

    public UserActivityResponse() {
    }

    public UserActivityResponse(
            Long activityId,
            Long userId,
            String userEmail,
            UserActivityType activityType,
            String description,
            String ipAddress,
            LocalDateTime activityDate
    ) {
        this.activityId = activityId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.activityType = activityType;
        this.description = description;
        this.ipAddress = ipAddress;
        this.activityDate = activityDate;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public UserActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(
            UserActivityType activityType
    ) {
        this.activityType = activityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(
            LocalDateTime activityDate
    ) {
        this.activityDate = activityDate;
    }
}