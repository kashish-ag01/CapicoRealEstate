package com.capico.user.dto.request;

import com.capico.user.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterUserRequest {

    @NotBlank(message = "First name is required")
    @Size(
            min = 2,
            max = 100,
            message = "First name must contain between 2 and 100 characters"
    )
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(
            min = 2,
            max = 100,
            message = "Last name must contain between 2 and 100 characters"
    )
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email address is not valid")
    @Size(
            max = 150,
            message = "Email must not exceed 150 characters"
    )
    private String email;

    @NotBlank(message = "Password is required")
    @Size(
            min = 8,
            max = 72,
            message = "Password must contain between 8 and 72 characters"
    )
    private String password;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile number must contain exactly 10 digits"
    )
    private String mobileNumber;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "City is required")
    @Size(
            min = 2,
            max = 100,
            message = "City must contain between 2 and 100 characters"
    )
    private String city;

    @NotBlank(message = "State is required")
    @Size(
            min = 2,
            max = 100,
            message = "State must contain between 2 and 100 characters"
    )
    private String state;

    @NotBlank(message = "Country is required")
    @Size(
            min = 2,
            max = 100,
            message = "Country must contain between 2 and 100 characters"
    )
    private String country;

    public RegisterUserRequest() {
    }

    public RegisterUserRequest(
            String firstName,
            String lastName,
            String email,
            String password,
            String mobileNumber,
            Gender gender,
            String city,
            String state,
            String country
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.country = country;
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
}