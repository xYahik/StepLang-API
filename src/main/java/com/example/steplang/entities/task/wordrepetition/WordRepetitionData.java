package com.example.steplang.entities.task.wordrepetition;

import com.example.steplang.entities.task.TaskDataBase;
import lombok.Data;

import java.util.List;

@Data
public class WordRepetitionData extends TaskDataBase {
    private List<WordRepetitionItem> itemList;
    private Long currentProgression;
}
