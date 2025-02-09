package org.example.studentsvoice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.CallerPrincipal;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.studentsvoice.cdi.producer.MyLogger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class CustomAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    @MyLogger
    private Logger LOGGER;

    @Inject
    private IdentityStore identityStore;

    @Inject
    private TokenBlacklist tokenBlacklist;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context) {
        String requestUri = request.getRequestURI();

        // Skip authentication for login endpoint
        if (isLoginRequest(requestUri)) {
            return handleLoginFlow(request, response, context);
        }

        // Handle token-based authentication
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return handleTokenAuthentication(authorizationHeader.substring(7), context);
        }

        LOGGER.info("No credentials provided. Proceeding with unauthenticated access.");
        return context.doNothing();
    }

    private boolean isLoginRequest(String uri) {
        return uri.endsWith("/auth/login");
    }

    private AuthenticationStatus handleLoginFlow(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context) {
        String username = (String) request.getAttribute("username");
        String password = (String) request.getAttribute("password");

        if (username == null || password == null) {
            // It's the first call to this method, so we just return doNothing()
            return context.doNothing();
        }

        UsernamePasswordCredential credential = new UsernamePasswordCredential(username, password);
        CredentialValidationResult result = identityStore.validate(credential);

        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            String token = JWTUtil.generateToken(result.getCallerPrincipal().getName(), result.getCallerGroups());
            response.setHeader("Authorization", token);

            LOGGER.info("User " + username + " successfully authenticated. Token issued.");
            return context.notifyContainerAboutLogin(result.getCallerPrincipal(), result.getCallerGroups());
        }

        LOGGER.warning("Invalid credentials for user: " + username);
        return context.responseUnauthorized();
    }

    private AuthenticationStatus handleTokenAuthentication(String token, HttpMessageContext context) {
        if (tokenBlacklist.isTokenBlacklisted(token)) {
            LOGGER.warning("Blacklisted token detected: " + token);
            return context.responseUnauthorized();
        }

        try {
            Claims claims = JWTUtil.parseToken(token);
            String username = claims.getSubject();

            @SuppressWarnings("unchecked")
            Set<String> roles = new HashSet<>((List<String>) claims.get("roles")); // Convert List to Set
            LOGGER.info("Authenticated using token for user: " + username);
            return context.notifyContainerAboutLogin(new CallerPrincipal(username), roles);
        } catch (JwtException e) {
            LOGGER.warning("Invalid or expired JWT token: " + e.getMessage());
            return context.responseUnauthorized();
        }
    }
}
