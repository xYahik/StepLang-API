package com.example.steplang.dtos.quest;

import lombok.Data;

@Data
public class QuestData_SpendTimeLearningDTO extends QuestDataDTO{
    private Double currentTime;
    private Double requiredTime;
}
