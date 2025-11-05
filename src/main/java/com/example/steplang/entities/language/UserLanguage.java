package com.example.steplang.entities.language;

import com.example.steplang.entities.User;
import com.example.steplang.entities.language.Language;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class UserLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Language language;

    @OneToMany(mappedBy = "userLanguage",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserWordProgress> words = new ArrayList<>();

    public UserLanguage(User user, Language language){
        this.user = user;
        this.language = language;
    }
}
