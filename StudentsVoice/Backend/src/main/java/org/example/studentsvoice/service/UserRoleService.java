package org.example.studentsvoice.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.studentsvoice.cdi.interceptor.Loggable;
import org.example.studentsvoice.entity.Role;
import org.example.studentsvoice.entity.UserRole;
import org.example.studentsvoice.repository.UserRoleRepository;

import java.util.List;
import java.util.Optional;

@Loggable
@ApplicationScoped
public class UserRoleService implements BaseService<UserRole, Long> {

    @Inject
    private UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public UserRole save(UserRole userRole, String... params) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public Optional<UserRole> findById(Long id) {
        return userRoleRepository.findById(id);
    }

    @Override
    public List<UserRole> findAll() {
        return userRoleRepository.findAll();
    }

    @Override
    public Optional<UserRole> update(UserRole UserRole) {
        return userRoleRepository.update(UserRole);
    }

    @Override
    public void deleteById(Long id) {
        userRoleRepository.deleteById(id);
    }

    public List<UserRole> findByUserId(Long userId) {
        return userRoleRepository.findByUserId(userId);
    }

    public List<Role> getRolesForUser(Long userId) {
        return findByUserId(userId).stream()
                .map(UserRole::getRole)
                .toList();
    }

    public List<UserRole> findByRoleCode(String roleCode) {
        return userRoleRepository.findByRoleCode(roleCode);
    }
}
