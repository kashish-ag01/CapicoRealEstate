package com.capico.user.entity;

import com.capico.user.enums.Gender;
import com.capico.user.enums.Role;
import com.capico.user.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "First name is required")
    @Size(
            min = 2,
            max = 100,
            message = "First name must contain between 2 and 100 characters"
    )
    @Column(
            name = "first_name",
            nullable = false,
            length = 100
    )
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(
            min = 2,
            max = 100,
            message = "Last name must contain between 2 and 100 characters"
    )
    @Column(
            name = "last_name",
            nullable = false,
            length = 100
    )
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Size(
            max = 150,
            message = "Email cannot exceed 150 characters"
    )
    @Column(
            name = "email",
            nullable = false,
            unique = true,
            length = 150
    )
    private String email;

    @NotBlank(message = "Password is required")
    @Column(
            name = "password",
            nullable = false,
            length = 255
    )
    @JsonIgnore
    private String password;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile number must contain exactly 10 digits"
    )
    @Column(
            name = "mobile_number",
            nullable = false,
            unique = true,
            length = 10
    )
    private String mobileNumber;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    @Column(
            name = "gender",
            nullable = false,
            length = 20
    )
    private Gender gender;

    @NotBlank(message = "City is required")
    @Size(
            max = 100,
            message = "City cannot exceed 100 characters"
    )
    @Column(
            name = "city",
            nullable = false,
            length = 100
    )
    private String city;

    @NotBlank(message = "State is required")
    @Size(
            max = 100,
            message = "State cannot exceed 100 characters"
    )
    @Column(
            name = "state",
            nullable = false,
            length = 100
    )
    private String state;

    @NotBlank(message = "Country is required")
    @Size(
            max = 100,
            message = "Country cannot exceed 100 characters"
    )
    @Column(
            name = "country",
            nullable = false,
            length = 100
    )
    private String country;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(
            name = "role",
            nullable = false,
            length = 20
    )
    private Role role;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private UserStatus status;

    @Column(
            name = "created_date",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdDate;

    public User() {
    }

    @PrePersist
    public void beforeSaving() {
        if (role == null) {
            role = Role.USER;
        }

        if (status == null) {
            status = UserStatus.ACTIVE;
        }

        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
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
        this.firstName = trim(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = trim(lastName);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null) {
            this.email = null;
        } else {
            this.email = email.trim().toLowerCase();
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = trim(mobileNumber);
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
        this.city = trim(city);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = trim(state);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = trim(country);
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}