package org.example.service;

import org.example.entity.Role;
import org.example.repository.RoleRepository;

public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService() {
        this.roleRepository = new RoleRepository();
    }

    public Role createRole(Role role) {
        return roleRepository.create(role);
    }

    public Role updateRole(Role role) {
        Role role1 = roleRepository.getById(role.getId());
        role1.setRole(role.getRole());
        return roleRepository.update(role1);
    }

    public boolean deleteRole(Long id) {
        return roleRepository.deleteById(id);
    }

    public Role getRole(Long id) {
        return roleRepository.getById(id);
    }
}
