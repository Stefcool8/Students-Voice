package org.example.studentsvoice.resource;

import io.jsonwebtoken.Claims;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.example.studentsvoice.cdi.producer.MyLogger;
import org.example.studentsvoice.dto.User.UserCreationAdminRequestDTO;
import org.example.studentsvoice.dto.User.UserRequestDTO;
import org.example.studentsvoice.dto.User.UserResponseAdminDTO;
import org.example.studentsvoice.entity.User;
import org.example.studentsvoice.mapper.UserMapper;
import org.example.studentsvoice.security.JWTService;
import org.example.studentsvoice.service.RoleService;
import org.example.studentsvoice.service.UserRoleService;
import org.example.studentsvoice.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/**
 * UserResource handles RESTful requests for user-related operations.
 * It enforces security constraints based on user roles (ADMIN, TEACHER, STUDENT).
 */
@DeclareRoles({"ADMIN", "TEACHER", "STUDENT"})
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
    private UserRoleService userRoleService;

    @Inject
    private RoleService roleService;

    @Inject
    private JWTService jwtService;

    @Inject
    private UserMapper userMapper;

    /**
     * Retrieves all users. Accessible only by ADMIN.
     *
     * @return Response containing the list of all users.
     */
    @GET
    @RolesAllowed("ADMIN")
    @Operation(summary = "Get all users", description = "Returns a list of all users in the database.")
    public Response getAllUsers() {
        try {
            LOGGER.info("Fetching all users");
            List<User> users = userService.findAll();
            List<UserResponseAdminDTO> usersDTO = userMapper.toAdminDTOList(users);
            return Response.ok(usersDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Retrieves a user by their ID. Accessible only by ADMIN.
     *
     * @param id The ID of the user.
     * @return Response containing the user details if found, or a NOT FOUND status.
     */
    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Get a user by ID", description = "Returns a user by their ID.")
    public Response getUserById(@PathParam("id") Long id) {
        try {
            LOGGER.info("Fetching user by ID: " + id);
            Optional<User> user = userService.findById(id);
            return user.map(value -> Response.ok(value).build())
                    .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Retrieves a user by their username. Accessible only by ADMIN.
     *
     * @param username The username of the user.
     * @return Response containing the user details if found, or a NOT FOUND status.
     */
    @GET
    @Path("/username/{username}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Get a user by username", description = "Returns a user by their username.")
    public Response getUserByUsername(@PathParam("username") String username) {
        try {
            LOGGER.info("Fetching user by username: " + username);
            Optional<User> user = userService.findByUsername(username);
            return user.map(value -> Response.ok(value).build())
                    .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Creates a new user. Accessible only by ADMIN.
     *
     * @param userDTO The user data to create.
     * @return Response containing the created user details.
     */
    @POST
    @RolesAllowed("ADMIN")
    @Operation(summary = "Create a new user", description = "Creates a new user in the database.")
    public Response createUser(@Valid UserCreationAdminRequestDTO userDTO) {
        try {
            LOGGER.info("Creating a new user");
            User user = userMapper.adminRequestDTOtoEntity(userDTO);
            User createdUser = userService.save(user, userDTO.getRole());

            UserResponseAdminDTO responseDTO = userMapper.toAdminDTO(createdUser);
            return Response.status(Response.Status.CREATED).entity(responseDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Updates an existing user by ID. Accessible only by ADMIN.
     *
     * @param id The ID of the user to update.
     * @param userDTO The updated user data.
     * @return Response indicating the update status.
     */
    @PUT
    @Path("{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Update a user", description = "Updates an existing user in the database.")
    public Response updateUser(@PathParam("id") Long id, @Valid UserCreationAdminRequestDTO userDTO) {
        try {
            LOGGER.info("Updating user with ID: " + id);
            User user = userMapper.adminRequestDTOtoEntity(userDTO);
            user.setId(id);
            User updatedUser = userService.update(user).orElseThrow(() -> new WebApplicationException("User not found.", Response.Status.NOT_FOUND));
            UserResponseAdminDTO responseDTO = userMapper.toAdminDTO(updatedUser);
            return Response.ok(responseDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Deletes a user by ID. Accessible only by ADMIN.
     *
     * @param id The ID of the user to delete.
     * @return Response indicating the deletion status.
     */
    @DELETE
    @Path("{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Delete a user", description = "Deletes an existing user by ID.")
    public Response deleteUser(@PathParam("id") Long id) {
        try {
            LOGGER.info("Deleting user with ID: " + id);
            userService.deleteById(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Retrieves all teacher names. Accessible by STUDENT, TEACHER, ADMIN.
     *
     * @return Response containing the list of all teacher names.
     */
    @GET
    @Path("/teacher-names")
    @RolesAllowed({"STUDENT", "TEACHER", "ADMIN"})
    @Operation(summary = "Get all teacher names", description = "Returns a list of all teacher names.")
    public Response getAllTeacherNames() {
        LOGGER.info("Fetching all teacher names");
        try {
            List<String> teacherNames = userRoleService.findByRoleCode("TEACHER").stream()
                    .map(userRole -> userRole.getUser().getName())
                    .toList();
            return Response.ok(teacherNames).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Retrieves a user. Accessible by STUDENT, TEACHER, ADMIN.
     *
     * @param request The HTTP request.
     * @return Response containing the user details if found, or a NOT FOUND status.
     */
    @GET
    @Path("/me")
    @RolesAllowed({"STUDENT", "TEACHER", "ADMIN"})
    @Operation(summary = "Get current user", description = "Returns the details of the current user.")
    public Response getCurrentUser(@Context HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        Response validationResponse = jwtService.validateToken(authorizationHeader);
        if (validationResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            // Return the validation response if the token is invalid
            return validationResponse;
        }

        Claims claims = (Claims) validationResponse.getEntity();
        String username = jwtService.extractUsername(claims);
        Set<String> roles = jwtService.extractRoles(claims);

        LOGGER.info("User " + username + " with roles " + roles + " is fetching their details");

        Optional<User> user = userService.findByUsername(username);
        return user.map(value -> Response.ok(userMapper.toUserResponseDTO(value)).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Updates the current user. Accessible by STUDENT, TEACHER, ADMIN.
     *
     * @param request The HTTP request.
     * @param userDTO The updated user data.
     * @return Response indicating the update status.
     */
    @PUT
    @Path("/me")
    @RolesAllowed({"STUDENT", "TEACHER", "ADMIN"})
    @Operation(summary = "Update current user", description = "Updates the details of the current user.")
    public Response updateCurrentUser(@Context HttpServletRequest request, @Valid UserRequestDTO userDTO) {
        String authorizationHeader = request.getHeader("Authorization");
        Response validationResponse = jwtService.validateToken(authorizationHeader);
        if (validationResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            // Return the validation response if the token is invalid
            return validationResponse;
        }

        Claims claims = (Claims) validationResponse.getEntity();
        String username = jwtService.extractUsername(claims);
        Set<String> roles = jwtService.extractRoles(claims);

        LOGGER.info("User " + username + " with roles " + roles + " is updating their details");

        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User updatedUser = userMapper.userRequestDTOtoEntity(userDTO);
        updatedUser.setId(user.get().getId());
        userService.update(updatedUser);

        return Response.ok(userMapper.toUserResponseDTO(updatedUser)).build();
    }
}
