package com.example.steplang.model.task.wordrepetition;

import com.example.steplang.model.task.TaskDataBase;
import lombok.Data;

import java.util.List;

@Data
public class WordRepetitionData extends TaskDataBase {
    private List<WordRepetitionItem> itemList;
    private Long currentProgression;
    private Long correctlyAnswered;
}
