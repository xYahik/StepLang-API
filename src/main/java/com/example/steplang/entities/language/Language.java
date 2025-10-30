package com.example.steplang.entities.language;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String Name;

    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL)
    List<Word> words = new ArrayList<Word>();
}
