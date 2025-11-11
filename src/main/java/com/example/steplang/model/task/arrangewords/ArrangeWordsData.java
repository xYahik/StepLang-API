package com.example.steplang.model.task.arrangewords;

import com.example.steplang.model.task.TaskDataBase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArrangeWordsData extends TaskDataBase {
    private List<ArrangeWordsDataItem> tasks;
    private Long currentProgression;
    private Long correctlyAnswered;
}