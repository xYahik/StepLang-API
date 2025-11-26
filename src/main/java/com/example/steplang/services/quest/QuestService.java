package com.example.steplang.services.quest;

import com.example.steplang.entities.quest.Quest;
import com.example.steplang.model.quest.QuestData_EarnExp;
import com.example.steplang.repositories.quest.QuestRepository;
import com.example.steplang.utils.enums.quest.QuestActionType;
import com.example.steplang.utils.enums.quest.QuestStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestService {

    private final QuestRepository questRepo;
    public void createTestQuest(){
        Quest q = new Quest();
        q.setUserId(1L);
        q.setType(QuestActionType.EARN_EXP);
        q.setStatus(QuestStatus.IN_PROGRESS);

        q.setData(new QuestData_EarnExp(5L, 10L));

        questRepo.save(q);
    }
}
