package org.example.studentsvoice.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.studentsvoice.cdi.interceptor.Loggable;
import org.example.studentsvoice.entity.Role;
import org.example.studentsvoice.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Loggable
@ApplicationScoped
public class RoleService implements BaseService<Role, Long> {

    @Inject
    private RoleRepository roleRepository;

    @Override
    public Role save(Role role, String... params) {
        // Check if the role already exists by name; if not, save it
        return roleRepository.findByName(role.getName())
                .orElseGet(() -> roleRepository.save(role));
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> update(Role role) {
        return roleRepository.update(role);
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    public Optional<Role> findByCode(String code) {
        return roleRepository.findByCode(code);
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
