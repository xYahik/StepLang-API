package com.example.steplang.model.quest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestData_SpendTimeLearning extends QuestData{
    private Long currentTimeSeconds;
    private Long requiredTimeSeconds;
}
