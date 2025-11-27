package com.example.steplang.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class QuestConfig {
    private final Long EarnExpMinValue = 10L;
    private final Long EarnExpMaxValue = 30L;

    private final Long SpendingTimeLearningMinMinutes = 10L;
    private final Long SpendingTimeLearningMaxMinutes = 30L;


    private final Double WeeklyQuestMultiplier = 2.5;
}
