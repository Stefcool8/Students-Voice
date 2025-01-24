package com.example.studentsvoice.userservice.aggregate;

import com.example.studentsvoice.core.command.DeleteUserCommand;
import com.example.studentsvoice.core.event.UserDeletedEvent;
import com.example.studentsvoice.userservice.handler.UserEventHandler;
import jakarta.inject.Inject;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateRoot;

@AggregateRoot
public class UserAggregate {

    @AggregateIdentifier
    private Long id;

    @Inject
    private UserEventHandler userEventHandler;

    public UserAggregate() {
    }

    @CommandHandler
    public UserAggregate(DeleteUserCommand command) {
        // Publish the User Deleted Event
        UserDeletedEvent userDeletedEvent = UserDeletedEvent.builder()
                .userId(command.getUserId())
                .build();
        AggregateLifecycle.apply(userDeletedEvent);
    }

    @EventSourcingHandler
    public void on(UserDeletedEvent event) {
        this.id = event.getUserId();
        userEventHandler.onUserDeletedEvent(event);
    }
}
