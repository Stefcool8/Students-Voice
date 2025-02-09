package org.example.studentsvoice.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.studentsvoice.entity.User;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class UserRepository implements BaseRepository<User, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        }
        return entityManager.merge(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public List<User> findAll() {
        return entityManager.createNamedQuery("User.findAll", User.class).getResultList();
    }

    @Override
    public Optional<User> update(User user) {
        return Optional.ofNullable(entityManager.merge(user));
    }

    @Override
    public void deleteById(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    public Optional<User> findByUsername(String username) {
        return entityManager.createNamedQuery("User.findByUsername", User.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return entityManager.createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    public Optional<User> findByName(String name) {
        return entityManager.createNamedQuery("User.findByName", User.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }

    public List<User> findByEnabled(boolean enabled) {
        return entityManager.createNamedQuery("User.findByEnabled", User.class)
                .setParameter("enabled", enabled)
                .getResultList();
    }
}
