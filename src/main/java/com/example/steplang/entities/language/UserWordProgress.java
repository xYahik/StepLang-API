package com.example.steplang.entities.language;

import com.example.steplang.utils.enums.UnderstandingLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
    private Long understandingProgress; //0-100

    private Instant nextRepetitionDate;

    public UserWordProgress(Word word, UserLanguage userLanguage, UnderstandingLevel understandingLevel, Long understandingProgress){
        this.word = word;
        this.userLanguage = userLanguage;
        this.understandingLevel = understandingLevel;
        this.understandingProgress = understandingProgress;
        this.nextRepetitionDate = Instant.now();
    }
}
