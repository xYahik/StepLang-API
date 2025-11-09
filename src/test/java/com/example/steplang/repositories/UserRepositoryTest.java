package com.example.steplang.repositories;

import com.example.steplang.entities.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class UserRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    @Order(1)
    public void testCreateUser(){
        userRepo.save(new User("TestUser1","testuser1@gmail.com","testPassword1"));
        assertThat(userRepo.findAll()).hasSize(1);
    }

    @Test
    @Order(2)
    public void FindUserTest(){
        userRepo.save(new User("TestUser2","testuser2@gmail.com","testPassword2"));
        assertThat(userRepo.findById(1L).orElse(null)).isNotNull();
        assertThat(userRepo.findByEmail("testuser1@gmail.com").orElse(null)).isNotNull();
        assertTrue(userRepo.existsByUsername("TestUser1"));
        assertFalse(userRepo.existsByEmail("testuser1@yahoo.com"));
    }
}
