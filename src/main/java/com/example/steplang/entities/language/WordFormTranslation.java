package com.example.steplang.entities.language;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WordFormTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private WordForm form;

    @ManyToOne
    private Language targetLanguage;

    private String translation;

    public WordFormTranslation(WordForm form, Language targetLanguage, String translation){
        this.form = form;
        this.targetLanguage = targetLanguage;
        this.translation = translation;
    }
}
