package com.example.steplang.services.quest;

import com.example.steplang.entities.quest.Quest;
import com.example.steplang.events.UserExpAddedEvent;
import com.example.steplang.model.quest.QuestData;
import com.example.steplang.model.quest.QuestData_EarnExp;
import com.example.steplang.model.quest.QuestData_SpendTimeLearning;
import com.example.steplang.repositories.quest.QuestRepository;
import com.example.steplang.utils.enums.quest.QuestActionType;
import com.example.steplang.utils.enums.quest.QuestIntervalType;
import com.example.steplang.utils.enums.quest.QuestStatus;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class QuestService {

    private final QuestRepository questRepo;
    public List<Quest> getUserQuests(Long userId){
        List<Quest> quests = new ArrayList<>();

        quests.addAll(getUserDailyQuests(userId));

        return quests;
    }
    public List<Quest> getUserDailyQuests(Long userId){
        List<Quest> dailyQuests = questRepo.findByUserIdAndValidUntilLessThanEqual(userId,calculateValidUntilTime(QuestIntervalType.DAILY));
        if(dailyQuests.size()<3){
            for(int currentQuests = dailyQuests.size();currentQuests<3;currentQuests++){
                dailyQuests.add(cenerateNewRandomQuest(userId,QuestIntervalType.DAILY));
            }
        }
        return dailyQuests;
    }
    @Transactional
    public Quest cenerateNewRandomQuest(Long userId, QuestIntervalType IntervalType){
        List<QuestActionType> availableQuests = List.of(QuestActionType.EARN_EXP,QuestActionType.SPEND_TIME_LEARNING);
        Quest quest = new Quest();
        quest.setUserId(userId);
        quest.setStatus(QuestStatus.IN_PROGRESS);
        quest.setIntervalType(IntervalType);
        quest.setValidUntil(calculateValidUntilTime(IntervalType));

        Random randomGenerator = new Random();
        quest.setType(availableQuests.get(randomGenerator.nextInt(availableQuests.size())));

        quest.setData(generateQuestData(quest.getType()));

        questRepo.save(quest);
        return quest;
    }

    private QuestData generateQuestData(QuestActionType type){
        switch(type){
            case QuestActionType.EARN_EXP -> {return new QuestData_EarnExp(0L,10L + (long) (Math.random() *(30L-10L)));}
            case QuestActionType.SPEND_TIME_LEARNING -> {return new QuestData_SpendTimeLearning(0D, 10D + (long) (Math.random() * (30D - 10D)));}
        }
        return new QuestData();
    }

    private Instant calculateValidUntilTime(QuestIntervalType intervalType) {
        switch (intervalType) {
            case QuestIntervalType.DAILY -> {
                ZonedDateTime now = ZonedDateTime.now()
                        .plusDays(1)
                        .withHour(0)
                        .withMinute(1)
                        .withSecond(0)
                        .withNano(0);
                return now.toInstant();
            }
            case QuestIntervalType.WEEKLY -> {
                ZonedDateTime now = ZonedDateTime.now()
                        .with(TemporalAdjusters.next(DayOfWeek.MONDAY)).
                        withHour(0)
                        .withMinute(1)
                        .withSecond(0)
                        .withNano(0);
                return now.toInstant();
            }
            case QuestIntervalType.MONTHLY -> {
                ZonedDateTime now = ZonedDateTime.now()
                        .with(TemporalAdjusters.firstDayOfNextMonth()).
                        withHour(0)
                        .withMinute(1)
                        .withSecond(0)
                        .withNano(0);
                return now.toInstant();
            }
        }
        return Instant.now();
    }
}
