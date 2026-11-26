package com.capico.user.dto.request;

import com.capico.user.enums.Role;
import com.capico.user.enums.UserStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateUserAccessRequest {

    @NotNull(message = "Role is required")
    private Role role;

    @NotNull(message = "Status is required")
    private UserStatus status;

    public UpdateUserAccessRequest() {
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
}