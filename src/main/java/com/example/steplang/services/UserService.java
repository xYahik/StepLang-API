package com.example.steplang.services;

import com.example.steplang.entities.User;
import com.example.steplang.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    public UserService(UserRepository repo){

        this.repo = repo;
    }

    @Transactional
    public User register(String username, String email, String password){
        if(repo.existsByUsername(username))
            throw new RuntimeException("UserName already used");
        if(repo.existsByEmail(email))
            throw new RuntimeException("Email already used");

        String hashedPassword = encoder.encode(password);
        User newUser = new User(username,email,hashedPassword);

        return repo.save(newUser);
    }
}
