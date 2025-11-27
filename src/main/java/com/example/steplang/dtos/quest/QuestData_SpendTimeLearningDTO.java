package com.example.steplang.dtos.quest;

import lombok.Data;

@Data
public class QuestData_SpendTimeLearningDTO extends QuestDataDTO{
    private Long currentTimeSeconds;
    private Long requiredTimeSeconds;
}
