package com.example.studentsvoice.userservice.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Embeddable
public class UserRoleId implements Serializable {
    private String username;
    private String role_code;
}
