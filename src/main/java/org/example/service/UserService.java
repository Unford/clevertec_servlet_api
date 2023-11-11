package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public User createUser(User user) {
        return userRepository.create(user);
    }

    public User updateUser(User user) {
       User user1 = userRepository.getById(user.getId());
       user1.setName(user.getName());
       user1.setRole(user.getRole());
       return userRepository.update(user1);
    }

    public boolean deleteUser(Long id) {
        return userRepository.deleteById(id);
    }

    public User getUser(Long id) {
        return userRepository.getById(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username).orElseThrow();
    }
}
