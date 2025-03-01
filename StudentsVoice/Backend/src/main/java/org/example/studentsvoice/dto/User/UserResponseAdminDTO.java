package org.example.studentsvoice.dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseAdminDTO {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String role;
    private boolean enabled;
}
