package com.example.steplang.mappers;

import com.example.steplang.dtos.language.WordGetDTO;
import com.example.steplang.entities.language.Word;
import com.example.steplang.entities.language.WordCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface WordMapper {

    @Mapping(source = "language.id", target = "languageId")
    @Mapping(source ="categories", target = "categoriesIds")
    WordGetDTO toDto(Word word);

    default List<Long> mapCategories(List<WordCategory> categories){
        if (categories == null)
            return List.of();
        return categories.stream()
                .map(WordCategory::getId)
                .collect(Collectors.toList());
    }
}
