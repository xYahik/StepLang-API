package com.example.steplang.dtos.task;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChooseWordWithImageDataDTO extends TaskDataBaseDTO {
    private ChooseWordWithImageWordDTO chosenWord;
    private List<ChooseWordWithImageWordDTO> extraWords;
}