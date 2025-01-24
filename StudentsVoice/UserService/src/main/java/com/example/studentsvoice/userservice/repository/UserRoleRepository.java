package com.example.studentsvoice.userservice.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import com.example.studentsvoice.userservice.entity.UserRole;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class UserRoleRepository implements BaseRepository<UserRole, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserRole save(UserRole userRole) {
        if (userRole.getId() == null) {
            entityManager.persist(userRole);
            return userRole;
        }
        return entityManager.merge(userRole);
    }

    @Override
    public Optional<UserRole> findById(Long id) {
        return Optional.ofNullable(entityManager.find(UserRole.class, id));
    }

    @Override
    public List<UserRole> findAll() {
        return entityManager.createNamedQuery("UserRole.findAll", UserRole.class).getResultList();
    }

    @Override
    public Optional<UserRole> update(UserRole usersRoles) {
        return Optional.ofNullable(entityManager.merge(usersRoles));
    }

    @Override
    public void deleteById(Long id) {
        UserRole userRole = entityManager.find(UserRole.class, id);
        if (userRole != null) {
            entityManager.remove(userRole);
        }
    }

    public List<UserRole> findByUserId(Long userId) {
        return entityManager.createNamedQuery("UserRole.findByUserId", UserRole.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<UserRole> findByRoleCode(String roleCode) {
        return entityManager.createNamedQuery("UserRole.findByRoleCode", UserRole.class)
                .setParameter("roleCode", roleCode)
                .getResultList();
    }
}
