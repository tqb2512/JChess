package com.tqb2512.jchess.service;

import com.tqb2512.jchess.storage.UserRepository;
import com.tqb2512.jchess.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    private final BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, MongoTemplate mongoTemplate, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public String signup(String username, String password) {
        if (username == null || password == null) {
            return "Invalid input";
        } else if (username.length() < 5 || password.length() < 5) {
            return "Username and password must be at least 4 characters long";
        }

        if (userRepository.findByUsername(username) == null) {
            String hashedPassword = passwordEncoder.encode(password);
            userRepository.save(new User(username, hashedPassword));
            return "User signed up";
        }
        return "User already exists";
    }

    public String signin(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return "User signed in";
        } else {
            return "Incorrect password";
        }
    }

    public void updateStats(String username, boolean win) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));

        Update update = new Update();
        if (win) {
            update.inc("wins", 1);
        } else {
            update.inc("losses", 1);
        }

        mongoTemplate.updateFirst(query, update, User.class);
    }

    public Map<String, User> getLeaderboard() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "wins"));
        List<User> users = mongoTemplate.find(query, User.class);
        return users.stream()
                .collect(Collectors.toMap(
                        User::getUsername,
                        user -> user,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }
}