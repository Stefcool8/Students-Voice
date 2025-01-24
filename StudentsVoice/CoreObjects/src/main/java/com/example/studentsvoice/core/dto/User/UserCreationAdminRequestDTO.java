package com.example.studentsvoice.core.dto.User;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationAdminRequestDTO {
    @NotBlank(message = "Username cannot be blank.")
    @Size(max = 50, message = "Username cannot exceed 50 characters.")
    private String username;

    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Name cannot be blank.")
    @Size(max = 100, message = "Name cannot exceed 50 characters.")
    private String name;

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 6, message = "Password must have at least 8 characters.")
    private String password;

    @NotBlank(message = "Role cannot be blank.")
    @Size(max = 50, message = "Role cannot exceed 50 characters.")
    private String role;

    @NotNull(message = "Enabled cannot be null.")
    private boolean enabled;
}
