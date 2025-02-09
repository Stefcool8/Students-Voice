package org.example.studentsvoice.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import org.example.studentsvoice.cdi.producer.MyLogger;
import org.example.studentsvoice.entity.Role;
import org.example.studentsvoice.entity.User;
import org.example.studentsvoice.service.UserRoleService;
import org.example.studentsvoice.service.UserService;

import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomIdentityStore implements IdentityStore {

    @Inject
    @MyLogger
    private Logger LOGGER;

    @Inject
    private UserService userService;

    @Inject
    private UserRoleService userRoleService;

    @Inject
    private BCryptPasswordHash passwordHash;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential usernamePasswordCredential) {
            String username = usernamePasswordCredential.getCaller();
            Password password = usernamePasswordCredential.getPassword();

            Optional<User> userOptional = userService.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                // Validate hashed password
                if (passwordHash.verify(password.getValue(), user.getPassword())) {
                    Set<String> roles = userRoleService.getRolesForUser(user.getId()).stream()
                            .map(Role::getCode)
                            .collect(Collectors.toSet());
                    LOGGER.info("User " + username + " has roles: " + roles);
                    return new CredentialValidationResult(user.getUsername(), roles);
                }
            }
        }
        return CredentialValidationResult.INVALID_RESULT;
    }
}
