package com.example.steplang.entities.language;

import com.example.steplang.utils.enums.UnderstandingLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class UserWordProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Word word;

    @ManyToOne
    private UserLanguage userLanguage;

    @Enumerated(EnumType.STRING)
    private UnderstandingLevel understandingLevel;

    @Min(0)
    @Max(100)
    private int understandingProgress; //0-100
}
