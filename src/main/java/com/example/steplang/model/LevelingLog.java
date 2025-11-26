package com.example.steplang.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@RedisHash(value = "levelingLog", timeToLive = 3600)
public class LevelingLog {

    @Id
    private String id;

    @Indexed
    private Long userId;

    @Indexed
    private Long languageId;

    @Indexed
    private Instant date;

    private Long previousLevel;
    private Long previousExperience;
    private Long currentLevel;
    private Long currentExperience;

}
