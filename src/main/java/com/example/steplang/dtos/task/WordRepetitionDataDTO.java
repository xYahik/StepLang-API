package com.example.steplang.dtos.task;

import lombok.Data;

import java.util.List;
@Data
public class WordRepetitionDataDTO extends TaskDataBaseDTO{
    private List<WordRepetitionItemDTO> itemList;
}
