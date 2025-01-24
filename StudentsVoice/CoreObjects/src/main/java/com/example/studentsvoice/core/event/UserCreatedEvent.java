package com.example.studentsvoice.core.event;

import com.example.studentsvoice.core.type.RoleCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreatedEvent {
    private String username;
    private String email;
    private String name;
    private String password;
    private RoleCode roleCode;
    private boolean enabled;
}
