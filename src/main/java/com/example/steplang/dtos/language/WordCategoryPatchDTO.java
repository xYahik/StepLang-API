package com.example.steplang.dtos.language;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WordCategoryPatchDTO {
    private String name;
}
