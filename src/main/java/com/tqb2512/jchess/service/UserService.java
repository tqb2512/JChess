package com.tqb2512.jchess.service;

import com.tqb2512.jchess.storage.UserRepository;
import com.tqb2512.jchess.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserService(UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public User getUser(String username) {
        Logger logger = Logger.getLogger(userRepository.findByUsername(username).toString());
        return userRepository.findByUsername(username);
    }

    public String signup(String username, String password) {
        if (userRepository.findByUsername(username) == null) {
            userRepository.save(new User(username, password));
            return "User signed up";
        }
        return "User already exists";
    }

    public String signin(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return "User signed in";
        } else {
            return "Incorrect password";
        }
    }

    public void updateStats(String username, boolean win) {
        User user = userRepository.findByUsername(username);
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
}