package com.example.steplang.mappers;

import com.example.steplang.dtos.user.UserMeDTO;
import com.example.steplang.dtos.user.UserProfileDTO;
import com.example.steplang.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserProfileDTO toProfileDto(User user);
    UserMeDTO toMeDto(User user);
}
