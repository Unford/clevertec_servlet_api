package ru.clevertec.course.web.repository;

import ru.clevertec.course.web.entity.Role;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class RoleRepository implements CRUDRepository<Role>{


    private static final AtomicLong id = new AtomicLong(0);
    private final Map<Long, Role> roles = new ConcurrentHashMap<>();

    @Override
    public Role create(Role role) {
        role.setId(id.incrementAndGet());
        roles.put(role.getId(), role);
        return role;
    }

    @Override
    public Role update(Role role) {
        roles.put(role.getId(), role);
        return role;
    }

    @Override
    public Optional<Role> findById(Long id) {
        return Optional.ofNullable(roles.get(id));
    }

    @Override
    public boolean deleteById(Long id) {
        return roles.remove(id) != null;
    }

    @Override
    public List<Role> findAll() {
        return new ArrayList<>(roles.values());
    }

    @Override
    public boolean isNameUnique(String name) {
        return roles.values().stream()
                .noneMatch(role -> role.getName().equals(name));
    }

    public Optional<Role> findByName(String name) {
        return roles.values().stream()
                .filter(role -> role.getName().equals(name))
                .findFirst();
    }
}
