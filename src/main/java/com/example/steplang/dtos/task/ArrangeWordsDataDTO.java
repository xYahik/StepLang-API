package com.example.steplang.dtos.task;

import lombok.Data;

import java.util.List;
@Data
public class ArrangeWordsDataDTO extends TaskDataBaseDTO{
    private List<ArrangeWordsDataItemDTO> tasks;
}
