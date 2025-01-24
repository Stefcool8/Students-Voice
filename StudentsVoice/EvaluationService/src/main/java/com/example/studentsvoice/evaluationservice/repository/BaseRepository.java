package com.example.studentsvoice.evaluationservice.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    Optional<T> update(T entity);
    void deleteById(ID id);
}
