package com.example.studentsvoice.core.command;

import lombok.Builder;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@Builder
public class CreateUserCommand {
    @TargetAggregateIdentifier
    Long id;
    String username;
    String email;
    String name;
    String password;
    boolean enabled;
}
