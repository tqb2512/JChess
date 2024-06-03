package com.tqb2512.jchess.service;

import com.tqb2512.jchess.storage.UserRepository;
import com.tqb2512.jchess.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String username) {
        Object user = userRepository.findByUsername(username);
        return user == null ? null : (User) user;
    }

    public String signup(String username, String password) {
        User user = new User(username, password);
        userRepository.findByUsername(username);
        if (user != null) {
            return "User already exists";
        } else {
            userRepository.save(user);
            return "User signed up";
        }
    }

    public String signin(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return "User signed in";
        } else {
            return "Incorrect password";
        }
    }
}