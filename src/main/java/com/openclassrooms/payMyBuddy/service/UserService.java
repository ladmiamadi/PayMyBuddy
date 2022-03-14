package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.TransactionsExceptions;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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

    public void updateUser(User user){
        userRepository.save(user);
    }

    public void deleteAddedUser(Integer id) {
        userRepository.deleteAddedUserById(id);
    }

    public boolean checkIfUserExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void encodePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    public User currentUser () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            return getUserByEmail(authentication.getName()).get();
        }
        return null;
    }

    public User registerUser(User user) {
        user.setBalance(BigDecimal.valueOf(0));
        user.setBankAccountBalance(HelperService.randomBalance());
        user.setRegistrationDate(HelperService.formattingNewDate());
        encodePassword(user);
        return addUser(user);
    }

    @Transactional(rollbackFor = TransactionsExceptions.class)
    public void deleteAddedUser(Integer id, User currentUser) throws TransactionsExceptions {
       if(userRepository.findById(id).isEmpty()) {
           throw new TransactionsExceptions("User not found");
       }
        User userToDelete = userRepository.findById(id).get();

       if(currentUser.getAddedUsers().contains(userToDelete)) {
            deleteAddedUser(id);
       } else {
           throw new TransactionsExceptions("Connection not found");
       }
    }

}
