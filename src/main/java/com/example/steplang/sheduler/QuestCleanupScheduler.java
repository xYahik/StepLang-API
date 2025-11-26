package com.example.steplang.sheduler;

import com.example.steplang.repositories.quest.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestCleanupScheduler {
    private final QuestRepository questRepo;

    @Scheduled(cron = "0 0 0 * * ?") //every midnight
    public void deleteExpiredQuests(){
        questRepo.deleteExpiredQuests();
    }
}
