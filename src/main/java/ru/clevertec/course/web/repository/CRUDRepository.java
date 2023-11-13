package ru.clevertec.course.web.repository;

import java.util.List;
import java.util.Optional;

public interface CRUDRepository<T> {

    T create(T t);

    T update(T t);

    Optional<T> findById(Long id);

    boolean deleteById(Long id);

    List<T> findAll();

    boolean isNameUnique(String name);
}
