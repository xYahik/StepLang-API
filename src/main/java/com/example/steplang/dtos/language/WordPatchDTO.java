package com.example.steplang.dtos.language;

import com.example.steplang.utils.enums.ReferenceLevel;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WordPatchDTO {
    private String word;
    private String translation;
    private ReferenceLevel referenceLevel;
    private Integer importanceLevel;
    private List<Long> CategoryIds;
}
