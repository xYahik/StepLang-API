package com.example.steplang.model.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskReward extends TaskStatusInfo{
    private Long earnedExp;

    public TaskReward(Boolean completed) {
        super(completed);
    }

    public TaskReward(Long earnedExp, Boolean completed) {
        super(completed);
        this.earnedExp = earnedExp;
    }
}
