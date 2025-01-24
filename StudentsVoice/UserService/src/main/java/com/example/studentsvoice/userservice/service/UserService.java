package com.example.studentsvoice.userservice.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import com.example.studentsvoice.userservice.cdi.interceptor.Loggable;
import com.example.studentsvoice.userservice.entity.Role;
import com.example.studentsvoice.userservice.entity.User;
import com.example.studentsvoice.userservice.entity.UserRole;
import com.example.studentsvoice.userservice.entity.UserRoleId;
import com.example.studentsvoice.userservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Loggable
@ApplicationScoped
public class UserService implements BaseService<User, Long> {

    @Inject
    private UserRepository userRepository;

    @Inject
    private RoleService roleService;

    @Inject
    private UserRoleService userRoleService;

    @Override
    @Transactional
    // Class 'UserService' must either be declared abstract or implement abstract method 'save(T, String...)' in 'BaseService'
    public User save(User user, String... params) {
        // Ensure username uniqueness before saving
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return null;
        }
        User createdUser = userRepository.save(user);

        // Save the user role
        String roleCode = params[0];
        UserRoleId userRoleId = new UserRoleId(createdUser.getUsername(), roleCode);
        Role role = roleService.findByCode(roleCode).orElseThrow(() -> new WebApplicationException("Role not found.", Response.Status.NOT_FOUND));
        userRoleService.save(new UserRole(userRoleId, createdUser, role));

        return createdUser;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> update(User user) {
        return userRepository.update(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public List<User> findByEnabled(boolean enabled) {
        return userRepository.findByEnabled(enabled);
    }
}

