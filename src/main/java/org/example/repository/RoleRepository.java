package org.example.repository;

import org.example.entity.Role;
import org.example.entity.Role;

import java.util.HashMap;
import java.util.Map;

public class RoleRepository implements CRUDRepository<Role>{


    private static Long id = 0L;

    private static Map<Long, Role> roles = new HashMap<>();

    @Override
    public Role create(Role role) {
        role.setId(++id);
        return roles.put(id, role);
    }

    @Override
    public Role update(Role role) {
        roles.put(role.getId(), role);
        return role;
    }

    @Override
    public Role getById(Long id) {
        return roles.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return roles.remove(id) != null;
    }
}
