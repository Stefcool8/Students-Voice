package org.example.studentsvoice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.example.studentsvoice.cdi.producer.MyLogger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class JWTService {

    @Inject
    @MyLogger
    private Logger LOGGER;

    /**
     * Parses the token and retrieves claims.
     *
     * @param token the JWT token
     * @return Claims object if the token is valid
     * @throws JwtException if the token is invalid or expired
     */
    public Claims parseToken(String token) throws JwtException {
        return JWTUtil.parseToken(token);
    }

    /**
     * Extracts roles from the token claims.
     *
     * @param claims the JWT claims
     * @return a set of roles
     */
    @SuppressWarnings("unchecked")
    public Set<String> extractRoles(Claims claims) {
        return new HashSet<>((List<String>) claims.get("roles"));
    }

    /**
     * Extracts the username from the token claims.
     *
     * @param claims the JWT claims
     * @return the username
     */
    public String extractUsername(Claims claims) {
        return claims.getSubject();
    }

    /**
     * Validates the Authorization header and returns claims.
     *
     * @param authorizationHeader the Authorization header value
     * @return Claims if the token is valid
     */
    public Response validateToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            LOGGER.warning("Authorization token missing or improperly formatted.");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Authorization token missing or improperly formatted")
                    .build();
        }

        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            return Response.ok(parseToken(token)).build();
        } catch (JwtException e) {
            LOGGER.warning("Invalid or expired JWT token: " + e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid or expired token")
                    .build();
        }
    }
}
