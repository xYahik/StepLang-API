package com.example.steplang.model.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordRepetitionStatusInfo extends TaskStatusInfo{
    private Long currentProgress;
    private Long maxProgress;

    public WordRepetitionStatusInfo(Boolean completed) {
        super(completed);
    }
    public WordRepetitionStatusInfo(Long currentProgress, Long maxProgress,Boolean completed) {
        super(completed);
        this.currentProgress = currentProgress;
        this.maxProgress = maxProgress;
    }
}
