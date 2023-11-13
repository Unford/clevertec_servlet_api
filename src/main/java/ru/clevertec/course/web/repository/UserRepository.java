package ru.clevertec.course.web.repository;

import ru.clevertec.course.web.entity.Role;
import ru.clevertec.course.web.entity.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepository implements CRUDRepository<User> {

    private static final AtomicLong id = new AtomicLong(0);

    private final Map<Long, User> users = new ConcurrentHashMap<>();

    @Override
    public User create(User user) {
        user.setId(id.incrementAndGet());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean deleteById(Long id) {
        return users.remove(id) != null;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean isNameUnique(String name) {
        return users.values().stream()
                .noneMatch(role -> role.getName().equals(name));
    }

    public Optional<User> getUserByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getName().equals(username))
                .findFirst();
    }

    public List<User> findAllByRole(Role role) {
        return users.values().stream()
                .filter(user -> user.getRoles().contains(role))
                .toList();
    }
}
