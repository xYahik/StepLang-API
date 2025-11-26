package com.example.steplang.model.quest;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Random;

@Data
@AllArgsConstructor
public class QuestData_EarnExp extends QuestData{
    private Long currentExp;
    private Long requiredExp;

    public QuestData_EarnExp(){
        this.currentExp = 0L;
        this.requiredExp = 10L + (long) (Math.random() * (30L - 10L));
    }
}
