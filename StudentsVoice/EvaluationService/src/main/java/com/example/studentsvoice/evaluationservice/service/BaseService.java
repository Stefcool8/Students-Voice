package com.example.studentsvoice.evaluationservice.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {
    // For the save method, add optional arguments that will be used only by UserService
    T save(T entity, String... args);
    Optional<T> findById(ID id);
    List<T> findAll();
    Optional<T> update(T entity);
    void deleteById(ID id);
}

