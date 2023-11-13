package ru.clevertec.course.web.service;

import lombok.AllArgsConstructor;
import ru.clevertec.course.web.entity.Role;
import ru.clevertec.course.web.exception.ResourceConflictException;
import ru.clevertec.course.web.exception.ResourceNotFoundException;
import ru.clevertec.course.web.repository.RoleRepository;
import ru.clevertec.course.web.repository.UserRepository;

import java.util.List;

@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public Role createRole(Role role) {
        String name = role.getName().toUpperCase();
        if (!roleRepository.isNameUnique(name)) {
            throw new ResourceConflictException("Role " + name + " already exist");
        }
        role.setName(name);
        return roleRepository.create(role);
    }

    public Role updateRole(Role updated) {
        Role role = this.findRoleById(updated.getId());
        if (role.getName().equalsIgnoreCase("admin")) {
            throw new ResourceConflictException("Can't update admin role");
        }
        if (updated.getName() != null && !updated.getName().isBlank()) {
            String name = updated.getName().toUpperCase();
            if (!roleRepository.isNameUnique(name)) {
                throw new ResourceConflictException("Role " + name + " already exist");
            }
            role.setName(name);
        }
        return roleRepository.update(role);
    }

    public boolean deleteRole(Long id) {
        Role role = this.findRoleById(id);
        if (!userRepository.findAllByRole(role).isEmpty()) {
            throw new ResourceConflictException(role + " has users");
        }
        return roleRepository.deleteById(id);
    }

    public Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role " + id + " not found"));
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
