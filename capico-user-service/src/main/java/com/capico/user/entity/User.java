package com.capico.user.entity;

import com.capico.user.enums.Gender;
import com.capico.user.enums.Role;
import com.capico.user.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(
                        name = "idx_users_email",
                        columnList = "email",
                        unique = true
                )
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    @Column(
            name = "first_name",
            nullable = false,
            length = 100
    )
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    @Column(
            name = "last_name",
            nullable = false,
            length = 100
    )
    private String lastName;
    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    @Column(
            name = "email",
            nullable = false,
            unique = true,
            length = 150
    )

    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must contain at least 8 characters")
    @Column(
            name = "password",
            nullable = false,
            length = 255
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Mobile number must be a valid 10-digit Indian mobile number"
    )
    @Column(
            name = "mobile_number",
            nullable = false,
            length = 15
    )
    private String mobileNumber;
    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    @Column(
            name = "gender",
            nullable = false,
            length = 30
    )
    private Gender gender;
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    @Column(
            name = "city",
            nullable = false,
            length = 100
    )
    private String city;
    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State cannot exceed 100 characters")
    @Column(
            name = "state",
            nullable = false,
            length = 100
    )
    private String state;
    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    @Column(
            name = "country",
            nullable = false,
            length = 100
    )
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "role",
            nullable = false,
            length = 20
    )
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private UserStatus status;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    public User() {
    }

    public User(
            Long userId,
            String firstName,
            String lastName,
            String email,
            String password,
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
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.country = country;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void setDefaultValuesBeforeSaving() {

        if (role == null) {
            role = Role.USER;
        }

        if (status == null) {
            status = UserStatus.ACTIVE;
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
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