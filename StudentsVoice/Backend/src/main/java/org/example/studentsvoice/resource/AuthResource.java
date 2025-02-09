package org.example.studentsvoice.resource;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.studentsvoice.cdi.producer.MyLogger;
import org.example.studentsvoice.security.JWTUtil;
import org.example.studentsvoice.security.TokenBlacklist;

import java.io.InputStream;
import java.util.logging.Logger;

@DeclareRoles({"ADMIN", "TEACHER", "STUDENT"})
@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    @MyLogger
    private Logger LOGGER;

    @Inject
    private TokenBlacklist tokenBlacklist;

    @POST
    @Path("/login")
    public Response login(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        try (InputStream inputStream = request.getInputStream();
             JsonReader jsonReader = Json.createReader(inputStream)) {
            JsonObject json = jsonReader.readObject();
            String username = json.getString("username", null);
            String password = json.getString("password", null);

            if (username == null || password == null) {
                LOGGER.warning("Invalid login request: Missing username or password.");
                return Response.status(Response.Status.BAD_REQUEST).entity("Missing username or password").build();
            }

            LOGGER.info("Attempting login for user: " + username);
            request.setAttribute("username", username);
            request.setAttribute("password", password);

            request.authenticate(response);

            String token = response.getHeader("Authorization");
            return Response.ok(Json.createObjectBuilder().add("token", token).build()).build();
        } catch (Exception e) {
            LOGGER.severe("Login failed: " + e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
    }

    @POST
    @Path("/logout")
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    public Response logout(@Context HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            // Add token to the blacklist
            tokenBlacklist.addToken(token);
            LOGGER.info("Token added to blacklist: " + token);
        }

        try {
            request.logout();
            request.getSession().invalidate();
            return Response.ok("Logout successful").build();
        } catch (ServletException e) {
            LOGGER.severe("Logout failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/user")
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    public Response getCurrentUser(@Context HttpServletRequest request) {
        LOGGER.info("Fetching current user details.");
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            try {
                Claims claims = JWTUtil.parseToken(token.substring(7));
                String username = claims.getSubject();

                return Response.ok(Json.createObjectBuilder()
                                .add("username", username)
                                .add("roles", claims.get("roles").toString())
                                .build())
                        .build();
            } catch (JwtException e) {
                LOGGER.warning("Invalid or expired JWT token: " + e.getMessage());
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Authorization token missing").build();
    }
//
//        String username = request.getUserPrincipal().getName();
//
//        Optional<User> user = userService.findByUsername(username);
//        if (user.isPresent()) {
//            UserResponseDTO userResponseDTO = userMapper.toDTO(user.get());
//            return Response.ok(userResponseDTO).build();
//        }
//
//        LOGGER.warning("No user found for username: " + username);
//        return Response.status(Response.Status.NOT_FOUND).build();
//    }
}
