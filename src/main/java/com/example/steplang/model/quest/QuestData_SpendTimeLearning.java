package com.example.steplang.model.quest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestData_SpendTimeLearning extends QuestData{
    private Double currentTime;
    private Double requiredTime;
}
