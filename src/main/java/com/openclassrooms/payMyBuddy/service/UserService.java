package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.model.Connection;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Iterable<User> getUsers () {
        return userRepository.findAll();
    }

    public Optional<User> getUserById (Integer id) {
        return userRepository.findById(id);
    }

    public User addUser (User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail (String email) {
        return userRepository.findByEmail(email);
    }

    public  Optional<User> getConnectionByEmail( String email) {
        return userRepository.findAddedUsersByEmail(email);
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser (User user) {
        userRepository.delete(user);
    }

    public void deleteUserById (Integer id) {
        userRepository.deleteById(id);
    }
}
