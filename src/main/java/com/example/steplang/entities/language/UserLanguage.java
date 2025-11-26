package com.example.steplang.entities.language;

import com.example.steplang.entities.User;
import com.example.steplang.entities.language.Language;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    private Long level;
    @Setter
    private Long experience;

    public UserLanguage(User user, Language language){
        this.user = user;
        this.language = language;
        this.level = 0L;
        this.experience = 0L;
    }
}
