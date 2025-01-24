package com.example.studentsvoice.core.command;

import lombok.Builder;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@Builder
public class DeleteUserCommand {
    @TargetAggregateIdentifier
    Long userId;
}
