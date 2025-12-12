package com.example.steplang.model.task.course;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChooseWordWithImageItem {
    private String word;
    private Long wordId;
    private Long formId;
    private String imageUrl;
}
