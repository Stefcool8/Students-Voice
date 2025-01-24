package com.example.studentsvoice.userservice.handler;

import com.example.studentsvoice.core.event.UserDeletedEvent;
import com.example.studentsvoice.userservice.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserEventHandler {

    @Inject
    private UserService userService;

    public void onUserDeletedEvent(UserDeletedEvent event) {
        userService.deleteById(event.getUserId());
    }
}
