package com.tqb2512.jchess.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
