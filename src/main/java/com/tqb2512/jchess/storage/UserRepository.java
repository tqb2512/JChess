package com.tqb2512.jchess.storage;

import com.tqb2512.jchess.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);

    @Query("{'username' : ?0}")
    void updateUserByUsername(String username, User user);
}