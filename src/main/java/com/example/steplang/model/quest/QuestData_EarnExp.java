package com.example.steplang.model.quest;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Random;

@Data
@AllArgsConstructor
public class QuestData_EarnExp extends QuestData{
    private Long currentExp;
    private Long requiredExp;
}
