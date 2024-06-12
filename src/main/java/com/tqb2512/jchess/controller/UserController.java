package com.tqb2512.jchess.controller;

import com.tqb2512.jchess.model.User;
import com.tqb2512.jchess.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/get")
    public ResponseEntity<User> getUser(@RequestParam String username) {
        User user = userService.getUser(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @RequestMapping("/getLeaderboard")
    public ResponseEntity<Map<String, User>> getLeaderboard() {
        return ResponseEntity.ok(userService.getLeaderboard());
    }

    @RequestMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String password) {
        return userService.signup(username, password);
    }

    @RequestMapping("/signin")
    public String signin(@RequestParam String username, @RequestParam String password) {
        return userService.signin(username, password);
    }
}
