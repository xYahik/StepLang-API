package com.example.steplang.services.quest;

import com.example.steplang.config.QuestConfig;
import com.example.steplang.entities.quest.Quest;
import com.example.steplang.events.TaskCompletedEvent;
import com.example.steplang.events.UserExpAddedEvent;
import com.example.steplang.model.quest.QuestData;
import com.example.steplang.model.quest.QuestData_EarnExp;
import com.example.steplang.model.quest.QuestData_SpendTimeLearning;
import com.example.steplang.model.task.LanguageTask;
import com.example.steplang.repositories.quest.QuestRepository;
import com.example.steplang.repositories.task.LanguageTaskRepository;
import com.example.steplang.utils.enums.quest.QuestActionType;
import com.example.steplang.utils.enums.quest.QuestIntervalType;
import com.example.steplang.utils.enums.quest.QuestStatus;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
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
    private final LanguageTaskRepository taskRepo;
    private final QuestConfig questConfig;
    public List<Quest> getUserQuests(Long userId){
        List<Quest> quests = new ArrayList<>();

        quests.addAll(getUserDailyQuests(userId));
        quests.addAll(getUserWeeklyQuests(userId));
        return quests;
    }
    private List<Quest> getUserDailyQuests(Long userId){
        List<Quest> dailyQuests = questRepo.findByUserIdAndIntervalType(userId,QuestIntervalType.DAILY);
        if(dailyQuests.size()<3){
            for(int currentQuests = dailyQuests.size();currentQuests<3;currentQuests++){
                dailyQuests.add(generateNewRandomQuest(userId,QuestIntervalType.DAILY));
            }
        }
        return dailyQuests;
    }
    private List<Quest> getUserWeeklyQuests(Long userId){
        List<Quest> weeklyQuests = questRepo.findByUserIdAndIntervalType(userId,QuestIntervalType.WEEKLY);
        if(weeklyQuests.size()<2){
            for(int currentQuests = weeklyQuests.size();currentQuests<2;currentQuests++){
                weeklyQuests.add(generateNewRandomQuest(userId,QuestIntervalType.WEEKLY));
            }
        }
        return weeklyQuests;
    }
    private List<Quest> getUserQuestsWithActionType(Long userId, QuestActionType actionType){
        List<Quest> quests = questRepo.findByUserIdAndType(userId,actionType);
        return quests;
    }
    @Transactional
    private Quest generateNewRandomQuest(Long userId, QuestIntervalType intervalType){
        List<QuestActionType> availableQuests = List.of(QuestActionType.EARN_EXP,QuestActionType.SPEND_TIME_LEARNING);
        Quest quest = new Quest();
        quest.setUserId(userId);
        quest.setStatus(QuestStatus.IN_PROGRESS);
        quest.setIntervalType(intervalType);
        quest.setValidUntil(calculateValidUntilTime(intervalType));

        Random randomGenerator = new Random();
        quest.setType(availableQuests.get(randomGenerator.nextInt(availableQuests.size())));

        quest.setData(generateQuestData(quest.getType(),intervalType));

        questRepo.save(quest);
        return quest;
    }

    private QuestData generateQuestData(QuestActionType type, QuestIntervalType intervalType){
        Double multiplier = 1.0;
        switch(intervalType){
            case QuestIntervalType.DAILY -> multiplier = 1.0;
            case QuestIntervalType.WEEKLY -> multiplier = questConfig.getWeeklyQuestMultiplier();
        }
        switch(type){
            case QuestActionType.EARN_EXP -> {return new QuestData_EarnExp(0L, (long) ((questConfig.getEarnExpMinValue()*multiplier + (long) (Math.random() *(questConfig.getEarnExpMaxValue()*multiplier-questConfig.getEarnExpMinValue()*multiplier)))));}
            case QuestActionType.SPEND_TIME_LEARNING -> {return new QuestData_SpendTimeLearning(0L, (long) ((questConfig.getSpendingTimeLearningMinMinutes()*multiplier + (long) (Math.random() * (questConfig.getSpendingTimeLearningMaxMinutes()*multiplier - questConfig.getSpendingTimeLearningMinMinutes()*multiplier))) * 60));}
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

    @Transactional
    @EventListener
    public void onUserGainedExp(UserExpAddedEvent event) {
        List<Quest> quests = getUserQuestsWithActionType(event.getUserId(),QuestActionType.EARN_EXP);

        for(Quest quest: quests){
            QuestData_EarnExp data = (QuestData_EarnExp) quest.getData();
            if(data.getCurrentExp() + event.getExp() < data.getRequiredExp()){
                data.setCurrentExp(data.getCurrentExp() + event.getExp());
            }else{
                data.setCurrentExp(data.getRequiredExp());
                quest.setStatus(QuestStatus.COMPLETED);
            }
        }

        questRepo.saveAll(quests);
    }
    @Transactional
    @EventListener
    public void onTaskCompleted(TaskCompletedEvent event){
        LanguageTask languageTask = taskRepo.findById(event.getTaskId()).orElse(null);
        Long taskCompletionTimeSeconds = Duration.between(languageTask.getTaskCreationTime(),Instant.now()).getSeconds();
        List<Quest> quests = getUserQuestsWithActionType(languageTask.getUserId(),QuestActionType.SPEND_TIME_LEARNING);

        for(Quest quest: quests){
            QuestData_SpendTimeLearning data = (QuestData_SpendTimeLearning) quest.getData();
            if(data.getCurrentTimeSeconds() + taskCompletionTimeSeconds < data.getRequiredTimeSeconds()){
                data.setCurrentTimeSeconds(data.getCurrentTimeSeconds() + taskCompletionTimeSeconds);
            }else{
                data.setCurrentTimeSeconds(data.getRequiredTimeSeconds());
                quest.setStatus(QuestStatus.COMPLETED);
            }
        }
        questRepo.saveAll(quests);
    }
}
