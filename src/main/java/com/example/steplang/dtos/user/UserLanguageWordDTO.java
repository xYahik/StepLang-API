package com.example.steplang.dtos.user;

import com.example.steplang.dtos.language.WordGetDTO;
import com.example.steplang.entities.language.UserLanguage;
import com.example.steplang.entities.language.Word;
import com.example.steplang.utils.enums.UnderstandingLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLanguageWordDTO {
    private WordGetDTO word;
    private UnderstandingLevel understandingLevel;
    private Long understandingProgress;
}
