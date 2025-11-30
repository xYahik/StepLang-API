package com.example.steplang.entities.language;

import com.example.steplang.utils.enums.language.WordGender;
import com.example.steplang.utils.enums.language.WordNumber;
import com.example.steplang.utils.enums.language.WordTense;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WordForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Word word;

    private String form;
    private Integer person;
    private WordNumber number;
    private WordGender gender;
    private WordTense tense;

    @OneToMany(mappedBy="form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WordFormTranslation> translations = new ArrayList<>();

    public WordForm(Word word, String form, Integer person, WordNumber number, WordGender gender, WordTense tense){
        this.word = word;
        this.form = form;
        this.person = person;
        this.number = number;
        this.gender = gender;
        this.tense = tense;
    }
}
