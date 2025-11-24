package com.example.steplang.services.user;

import com.example.steplang.entities.language.UserLanguage;
import com.example.steplang.events.UserExpAddedEvent;
import com.example.steplang.model.LevelingLog;
import com.example.steplang.repositories.LevelingLogRepository;
import com.example.steplang.repositories.language.UserLanguageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LevelingService {
    private final UserLanguageRepository userLanguageRepo;
    private final LevelingLogRepository levelingLogRepository;
    private final ApplicationEventPublisher publisher;
    @Transactional
    public void AddUserLanguageXP(Long userId, Long languageId, Long experience){
        UserLanguage userLanguage = userLanguageRepo.findByLanguageIdAndUserId(languageId,userId).orElse(null);

        if(userLanguage == null)
            throw new RuntimeException(String.format("User with id = '%d' is not learning language with id ='%d'",userId,languageId));

        Long previousLevel = userLanguage.getLevel();
        Long previousExperience = userLanguage.getExperience();
        Long experienceForNextLevel = ExperienceRequiredForNextLevel(userLanguage.getLevel());
        if(userLanguage.getExperience() + experience >= experienceForNextLevel){
            userLanguage.setExperience(userLanguage.getExperience() + experience - experienceForNextLevel);
            userLanguage.setLevel(userLanguage.getLevel() +1);
        }else{
            userLanguage.setExperience(userLanguage.getExperience() + experience);
        }
        publisher.publishEvent(new UserExpAddedEvent(userId,languageId,experience));
        userLanguageRepo.save(userLanguage);

        LevelingLog newLog = new LevelingLog(UUID.randomUUID().toString(),userId,languageId, Instant.now(),previousLevel,previousExperience,userLanguage.getLevel(),userLanguage.getExperience());
        levelingLogRepository.save(newLog);
    }

    public Long ExperienceRequiredForNextLevel(Long currentLevel){
        return (long) (6 * Math.pow(currentLevel,1.5));
    }

    public LevelingLog getLatestLevelingLog(Long userId, Long languageId){
        return levelingLogRepository.findByUserIdAndLanguageIdOrderByDateDesc(userId,languageId)
                .stream()
                .findFirst().orElse(null);
    }
}
