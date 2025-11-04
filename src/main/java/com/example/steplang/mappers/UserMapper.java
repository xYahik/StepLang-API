package com.example.steplang.mappers;

import com.example.steplang.dtos.user.UserAuthInfoDTO;
import com.example.steplang.dtos.user.UserLanguageWordDTO;
import com.example.steplang.dtos.user.UserMeDTO;
import com.example.steplang.dtos.user.UserProfileDTO;
import com.example.steplang.entities.User;
import com.example.steplang.entities.language.UserWordProgress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { WordMapper.class })
public interface UserMapper {
    UserProfileDTO toProfileDto(User user);
    UserMeDTO toMeDto(User user);
    UserAuthInfoDTO toAuthInfoDto(User user);

    UserLanguageWordDTO toUserLanguageWordDto(UserWordProgress userWordProgress);
}
