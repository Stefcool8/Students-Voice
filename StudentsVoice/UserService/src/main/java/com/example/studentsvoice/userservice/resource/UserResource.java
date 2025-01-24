package com.example.studentsvoice.userservice.resource;

import com.example.studentsvoice.core.command.DeleteUserCommand;
import com.example.studentsvoice.userservice.cdi.producer.MyLogger;
import com.example.studentsvoice.userservice.entity.User;
import com.example.studentsvoice.userservice.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.axonframework.commandhandling.gateway.CommandGateway;

import java.util.List;
import java.util.logging.Logger;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    @MyLogger
    private Logger LOGGER;

    @Inject
    private UserService userService;

    @Inject
    private CommandGateway commandGateway;

    @GET
    public List<User> getUsers() {
        return userService.findAll();
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        LOGGER.info("Deleting user with ID: " + id);

        DeleteUserCommand deleteUserCommand = DeleteUserCommand.builder()
                .userId(id)
                .build();

        Object result = commandGateway.sendAndWait(deleteUserCommand);
        return Response.ok(result).build();
    }
}
