package com.example.studentsvoice.userservice.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import com.example.studentsvoice.userservice.entity.Role;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class RoleRepository implements BaseRepository<Role, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role save(Role role) {
        if (role.getId() == null) {
            entityManager.persist(role);
            return role;
        }
        return entityManager.merge(role);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Role.class, id));
    }

    @Override
    public List<Role> findAll() {
        return entityManager.createNamedQuery("Role.findAll", Role.class).getResultList();
    }

    @Override
    public Optional<Role> update(Role role) {
        return Optional.ofNullable(entityManager.merge(role));
    }

    @Override
    public void deleteById(Long id) {
        Role role = entityManager.find(Role.class, id);
        if (role != null) {
            entityManager.remove(role);
        }
    }

    public Optional<Role> findByCode(String code) {
        return entityManager.createNamedQuery("Role.findByCode", Role.class)
                .setParameter("code", code)
                .getResultStream()
                .findFirst();
    }

    public Optional<Role> findByName(String name) {
        return entityManager.createNamedQuery("Role.findByName", Role.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }
}

