package com.example.steplang.dtos.quest;

import com.example.steplang.model.quest.QuestData;
import com.example.steplang.utils.enums.quest.QuestActionType;
import com.example.steplang.utils.enums.quest.QuestIntervalType;
import com.example.steplang.utils.enums.quest.QuestStatus;
import lombok.Data;

@Data
public class QuestDTO {
    private QuestActionType type;
    private QuestIntervalType intervalType;
    private QuestStatus status;
    private QuestDataDTO data;
}
