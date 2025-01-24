package com.example.studentsvoice.core.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDeletedEvent {
    private Long userId; // Identifier for the deleted user
}
