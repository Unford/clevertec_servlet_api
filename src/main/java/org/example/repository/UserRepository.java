package org.example.repository;

import org.example.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserRepository implements CRUDRepository<User>{

    private static Long id = 0L;

    private static Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        user.setId(++id);
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public User update(User user) {
        users.put(user.getId() ,user);
        return user;
    }

    @Override
    public User getById(Long id) {
        return users.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return users.remove(id) != null;
    }

    public Optional<User> getUserByUsername(String username) {
        Optional<User> users1 = users.values().stream()
                .filter(user -> user.getName().equals(username))
                .findFirst();

        return users1;
    }
}
