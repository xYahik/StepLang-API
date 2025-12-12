package com.example.steplang.entities.language;

import com.example.steplang.utils.enums.ReferenceLevel;
import com.example.steplang.utils.enums.language.WordType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    private String baseForm;
    //private String translation;

    @Enumerated(EnumType.STRING)
    private ReferenceLevel referenceLevel; //A1-C2

    private Integer importanceLevel; // 1-100 | 100 is most important

    private WordType wordType;

    @ManyToMany
    @JoinTable(
            name = "word_categories",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name ="category_id")
    )
    private List<WordCategory> categories;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WordForm> forms = new ArrayList<>();

    private String imageUrl;
    public Word(Language lang,String baseForm,  WordType wordType){
        this.language = lang;
        this.baseForm = baseForm;
        this.wordType = wordType;
    }

    public void AddCategory(WordCategory category){
        categories.add(category);
    }

    public void addWordForm(WordForm wordForm){ forms.add(wordForm);}
}
