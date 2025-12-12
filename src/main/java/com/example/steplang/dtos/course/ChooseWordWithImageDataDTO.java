package com.example.steplang.dtos.course;

import com.example.steplang.dtos.task.TaskDataBaseDTO;
import com.example.steplang.model.task.course.ChooseWordWithImageItem;
import lombok.Data;

import java.util.List;
@Data
public class ChooseWordWithImageDataDTO extends TaskDataBaseDTO {
    private String wordImageUrl;
    private List<ChooseWordWithImageItemDTO> wordsList;
}
