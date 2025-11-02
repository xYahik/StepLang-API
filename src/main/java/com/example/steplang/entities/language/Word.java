package com.example.steplang.entities.language;

import com.example.steplang.utils.enums.ReferenceLevel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mapstruct.EnumMapping;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    @Column(name = "word_id")
    private Long wordId;

    private String word;
    private String translation;

    @Enumerated(EnumType.STRING)
    private ReferenceLevel referenceLevel; //A1-C2

    private Integer importanceLevel; // 1-100 | 100 is most important

    @ManyToMany
    @JoinTable(
            name = "word_categories",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name ="category_id")
    )
    private List<WordCategory> categories;

    public Word(Language lang,String word, String translation){
        this.language = lang;
        this.word = word;
        this.translation = translation;
    }

    public void AddCategory(WordCategory category){
        categories.add(category);
    }
}
