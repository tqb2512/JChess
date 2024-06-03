package com.tqb2512.jchess.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String username;
    private String password;
    private int totalGames;
    private int wins;
    private int losses;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.totalGames = 0;
        this.wins = 0;
        this.losses = 0;
    }
}
