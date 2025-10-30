package com.example.steplang.entities.language;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    private String word;
    private String translation;



}
