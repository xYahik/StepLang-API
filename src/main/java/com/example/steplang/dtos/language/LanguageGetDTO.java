package com.example.steplang.dtos.language;

import lombok.Data;

import java.util.List;
@Data
public class LanguageGetDTO {
    private Long id;

    private String name;

    List<WordGetDTO> words;
}
