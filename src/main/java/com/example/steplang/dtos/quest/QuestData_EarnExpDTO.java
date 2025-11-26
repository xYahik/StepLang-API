package com.example.steplang.dtos.quest;

import lombok.Data;

@Data
public class QuestData_EarnExpDTO extends QuestDataDTO{
    private Long currentExp;
    private Long requiredExp;
}
