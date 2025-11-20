package com.example.steplang.model.task.arrangewords;

import com.example.steplang.model.task.TaskReward;
import com.example.steplang.model.task.TaskStatusInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArrangeWordsStatusInfo extends TaskStatusInfo {
    private Long currentProgress;
    private Long maxProgress;

    public ArrangeWordsStatusInfo(Boolean completed) {
        super(completed);
    }
    public ArrangeWordsStatusInfo(Long currentProgress, Long maxProgress,Boolean completed) {
        super(completed);
        this.currentProgress = currentProgress;
        this.maxProgress = maxProgress;
    }
}