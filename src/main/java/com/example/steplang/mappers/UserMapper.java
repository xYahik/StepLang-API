package com.example.steplang.mappers;

import com.example.steplang.dtos.user.*;
import com.example.steplang.entities.User;
import com.example.steplang.entities.language.UserWordProgress;
import com.example.steplang.model.LevelingLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { WordMapper.class })
public interface UserMapper {
    UserProfileDTO toProfileDto(User user);
    UserMeDTO toMeDto(User user);
    UserAuthInfoDTO toAuthInfoDto(User user);

    UserLanguageWordDTO toUserLanguageWordDto(UserWordProgress userWordProgress);

    LevelingLogDTO toLevelingLogDto(LevelingLog levelingLog);
}
