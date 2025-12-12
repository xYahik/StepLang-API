package com.example.steplang.dtos.course;

import com.example.steplang.dtos.task.TaskDataBaseDTO;
import lombok.Data;

@Data
public class ChooseWordWithImageInfoDTO {

    private String taskId;
    TaskDataBaseDTO taskData;
}
