package com.example.steplang.entities;

import com.example.steplang.entities.language.UserLanguage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.*;

@Entity
@NoArgsConstructor
@Table(name = "users")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private int level = 1;
    private int exp = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @OneToMany(mappedBy="user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLanguage> languages = new ArrayList<>();

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
