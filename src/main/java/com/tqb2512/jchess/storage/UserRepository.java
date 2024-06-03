package com.tqb2512.jchess.storage;

import com.tqb2512.jchess.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}