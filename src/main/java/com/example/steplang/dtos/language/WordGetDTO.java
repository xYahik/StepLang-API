package com.example.steplang.dtos.language;

import com.example.steplang.entities.language.Language;
import com.example.steplang.entities.language.WordCategory;
import com.example.steplang.utils.enums.ReferenceLevel;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class WordGetDTO {
    private Long id;
    private Long languageId;
    private Long wordId;

    private String word;
    private String translation;
    private ReferenceLevel referenceLevel;

    private Integer importanceLevel;
    private List<Long> categoriesIds;

}
