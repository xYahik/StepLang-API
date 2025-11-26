package com.example.steplang.mappers;

import com.example.steplang.dtos.quest.QuestDTO;
import com.example.steplang.dtos.quest.QuestDataDTO;
import com.example.steplang.dtos.quest.QuestData_EarnExpDTO;
import com.example.steplang.dtos.task.ArrangeWordsDataDTO;
import com.example.steplang.dtos.task.TaskDataBaseDTO;
import com.example.steplang.dtos.task.WordRepetitionDataDTO;
import com.example.steplang.entities.quest.Quest;
import com.example.steplang.model.quest.QuestData;
import com.example.steplang.model.quest.QuestData_EarnExp;
import com.example.steplang.model.task.TaskDataBase;
import com.example.steplang.model.task.arrangewords.ArrangeWordsData;
import com.example.steplang.model.task.wordrepetition.WordRepetitionData;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestMapper {
    QuestDTO toDto(Quest quests);
    List<QuestDTO> toDtoList(List<Quest> quests);

    @SubclassMapping(source = QuestData_EarnExp.class, target = QuestData_EarnExpDTO.class)
    QuestDataDTO mapQuestData(QuestData taskData);
}
