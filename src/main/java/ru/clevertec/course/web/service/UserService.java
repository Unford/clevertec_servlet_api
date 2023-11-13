package ru.clevertec.course.web.service;

import lombok.AllArgsConstructor;
import ru.clevertec.course.web.entity.Role;
import ru.clevertec.course.web.entity.User;
import ru.clevertec.course.web.exception.ResourceConflictException;
import ru.clevertec.course.web.exception.ResourceNotFoundException;
import ru.clevertec.course.web.repository.RoleRepository;
import ru.clevertec.course.web.repository.UserRepository;
import ru.clevertec.course.web.validator.UserValidator;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserValidator validator;


    public User createUser(User user) {
        validator.validateCreate(user);
        if (!userRepository.isNameUnique(user.getName())) {
            throw new ResourceConflictException("Username " + user.getName() + " already in use");
        }
        Set<Role> roles = getCreatedRoles(user.getRoles());
        if (roles.isEmpty()) {
            throw new ResourceNotFoundException("All user roles don't exist");
        }
        user.setRoles(roles);
        return userRepository.create(user);
    }



    private Set<Role> getCreatedRoles(Set<Role> roles) {
        return roles.stream()
                .map(role -> roleRepository.findByName(role.getName().toUpperCase()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public User updateUser(User updateUser) {
        validator.validateUpdate(updateUser);

        User user = this.findUserById(updateUser.getId());

        if (updateUser.getPassword() != null) {
            user.setPassword(user.getPassword());
        }
        if (updateUser.getName() != null) {
            if (!userRepository.isNameUnique(updateUser.getName())) {
                throw new ResourceConflictException("Username " + user.getName() + " already in use");
            }
            user.setName(user.getName());

        }
        if (updateUser.getRoles() != null && !updateUser.getRoles().isEmpty()) {
            Set<Role> roles = getCreatedRoles(user.getRoles());
            if (roles.isEmpty()) {
                throw new ResourceNotFoundException("All user roles " + updateUser.getRoles() + " don't exist");
            }
            user.setRoles(roles);
        }
        return userRepository.update(user);
    }

    public boolean deleteUser(Long id) {
        this.findUserById(id);
        return userRepository.deleteById(id);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found"));
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
