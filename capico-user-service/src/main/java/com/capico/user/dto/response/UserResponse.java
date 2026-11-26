package com.capico.user.dto.response;

import com.capico.user.enums.Gender;
import com.capico.user.enums.Role;
import com.capico.user.enums.UserStatus;

import java.time.LocalDateTime;

public class UserResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private Gender gender;
    private String city;
    private String state;
    private String country;
    private Role role;
    private UserStatus status;
    private LocalDateTime createdAt;

    public UserResponse() {
    }

    public UserResponse(
            Long userId,
            String firstName,
            String lastName,
            String email,
            String mobileNumber,
            Gender gender,
            String city,
            String state,
            String country,
            Role role,
            UserStatus status,
            LocalDateTime createdAt
    ) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.country = country;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}