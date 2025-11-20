package com.example.steplang.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LevelingLogDTO {
    private Long previousLevel;
    private Long previousExperience;
    private Long currentLevel;
    private Long currentExperience;
}
