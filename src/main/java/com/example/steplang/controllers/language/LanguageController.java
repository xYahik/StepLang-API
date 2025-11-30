package com.example.steplang.controllers.language;

import com.example.steplang.commands.language.*;
import com.example.steplang.dtos.language.*;
import com.example.steplang.entities.language.*;
import com.example.steplang.errors.LanguageError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.mappers.LanguageMapper;
import com.example.steplang.mappers.WordMapper;
import com.example.steplang.services.language.LanguageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/language")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;
    private final WordMapper wordMapper;
    private final LanguageMapper languageMapper;

    //Languages
    @PostMapping("/add")
    public ResponseEntity<?> addNewLanguage(@Valid @RequestBody AddNewLanguageCommand command){
        Language language = languageService.addNewLanguage(command);
        return ResponseEntity.ok(languageMapper.toDto(language));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchLanguage(@PathVariable Long id,@RequestBody LanguagePatchDTO dto){
        Language language = languageService.patchLanguage(id, dto);
        return ResponseEntity.ok(languageMapper.toDto(language));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getLanguage(@PathVariable Long id) {
        Language language = languageService.getLanguageById(id);
        if(language == null)
            throw new ApiException(LanguageError.LANGUAGE_ID_NOT_FOUND,String.format("Language with id = '%d' doesn't exist", id));

        return ResponseEntity.ok(languageMapper.toDto(language));
    }
    @GetMapping("/{id}/words")
    public ResponseEntity<?> getLanguageWordsList(@PathVariable Long id){
        Language language = languageService.getLanguageById(id);
        if(language == null)
            throw new ApiException(LanguageError.LANGUAGE_ID_NOT_FOUND,String.format("Language with id = '%d' doesn't exist", id));

        List<WordGetDTO> wordsList = languageService.getWordsList(id).stream()
                .map(wordMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(wordsList);
    }


    //Words
    @PostMapping("{languageId}/word/add")
    public ResponseEntity<?> addNewLanguageWord(@PathVariable Long languageId, @Valid @RequestBody CreateNewWordCommand command){
        command.setLanguageId(languageId);
        Word word = languageService.addNewWord(command);
        return ResponseEntity.ok(wordMapper.toDto(word));
    }

    @PostMapping("{languageId}/word/form/add")
    public ResponseEntity<?> addNewLanguageWordForm(@PathVariable Long languageId, @Valid @RequestBody AddNewWordFormCommand command){
        command.setLanguageId(languageId);
        WordForm wordForm = languageService.addNewWordForm(command);
        return ResponseEntity.ok("");
    }
    @PostMapping("{languageId}/word/form/translation/add")
    public ResponseEntity<?> addNewLanguageWordFormTranslation(@PathVariable Long languageId, @Valid @RequestBody AddNewWordFormTranslationCommand command){
        command.setLanguageId(languageId);
        WordFormTranslation wordFormTranslation = languageService.addNewWordFormTranslation(command);
        return ResponseEntity.ok("");
    }
    @PostMapping("{id}/{wordId}/category/add")
    public ResponseEntity<?> addCategoryToWord(@PathVariable Long id, @PathVariable Long wordId, @RequestBody AddCategoryToWordCommand command){
        String message = languageService.addCategoryToWord(id,wordId,command);
        return ResponseEntity.ok(Map.of("message",message));
    }

    @GetMapping("/{id}/{wordId}")
    public ResponseEntity<?> getWord(@PathVariable Long id, @PathVariable Long wordId){

        Word word = languageService.getWordByLanguageIdAndWordId(id, wordId);
        if(word == null)
            throw new ApiException(LanguageError.LANGUAGE_AND_WORD_ID_NOT_FOUND,String.format("Couldn't find word for languageId = %d and wordId = %d",id, wordId));

        return ResponseEntity.ok(wordMapper.toDto(word));
    }

    @PatchMapping("/{id}/{wordId}")
    public ResponseEntity<?> patchWord(@PathVariable Long id, @PathVariable Long wordId, @RequestBody WordPatchDTO dto){
        Word word = languageService.patchWord(id, wordId,dto);
        return ResponseEntity.ok(wordMapper.toDto(word));
    }

    //Categories
    @PostMapping("/category/add")
    public ResponseEntity<?> addNewCategory(@Valid @RequestBody CreateNewCategoryCommand command){
        languageService.addNewCategory(command);
        return ResponseEntity.ok(Map.of("message",String.format("Category '%s' added successfully", command.getName())));
    }

    @PatchMapping("category/{categoryId}")
    public ResponseEntity<?> patchWordCategory(@PathVariable Long categoryId, @RequestBody WordCategoryPatchDTO dto){
        WordCategory wordCategory = languageService.patchWordCategory(categoryId, dto);
        return ResponseEntity.ok(wordCategory);
    }
    @GetMapping("category/{categoryId}")
    public ResponseEntity<?> patchWordCategory(@PathVariable Long categoryId){
        WordCategory wordCategory = languageService.getWordCategory(categoryId);
        return ResponseEntity.ok(wordCategory);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLanguage(@PathVariable Long id){
        boolean deleted = languageService.deleteLanguageById(id);
        if(deleted)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/")
    public ResponseEntity<?> deleteLanguages(@RequestBody List<Long> ids){
       languageService.deleteAllLanguagesByIds(ids);
            return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}/{wordId}")
    public ResponseEntity<?> deleteWord(@PathVariable Long id,@PathVariable Long wordId){
        boolean deleted = languageService.deleteWordById(id,wordId);
        if(deleted)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}/words")
    public ResponseEntity<?> deleteWords(@PathVariable Long id, @RequestBody List<Long> wordIds){
        languageService.deleteAllWordsByIds(id,wordIds);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId){
        boolean deleted = languageService.deleteCategoryById(categoryId);
        if(deleted)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/category")
    public ResponseEntity<?> deleteCategories(@RequestBody List<Long> categoryIds){
        languageService.deleteAllCategoriesByIds(categoryIds);
        return ResponseEntity.noContent().build();
    }

}
