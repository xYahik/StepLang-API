package com.example.steplang.services.language;

import com.example.steplang.commands.language.AddCategoryToWordCommand;
import com.example.steplang.commands.language.AddNewLanguageCommand;
import com.example.steplang.commands.language.CreateNewCategoryCommand;
import com.example.steplang.commands.language.CreateNewWordCommand;
import com.example.steplang.dtos.language.LanguagePatchDTO;
import com.example.steplang.dtos.language.WordCategoryPatchDTO;
import com.example.steplang.dtos.language.WordPatchDTO;
import com.example.steplang.entities.language.Language;
import com.example.steplang.entities.language.Word;
import com.example.steplang.entities.language.WordCategory;
import com.example.steplang.errors.LanguageError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.repositories.language.LanguageRepository;
import com.example.steplang.repositories.language.WordCategoryRepository;
import com.example.steplang.repositories.language.WordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepository languageRepo;
    private final WordRepository wordRepo;
    private final WordCategoryRepository wordCategoryRepo;
    @Transactional
    public Language addNewLanguage(AddNewLanguageCommand command){
        if(languageRepo.existsByName(command.getName())){
            throw new ApiException(LanguageError.ALREADY_EXISTS,String.format("Language with name '%s' already exists", command.getName()));
        }
        Language language = new Language(command.getName());
        languageRepo.save(language);
        return language;
    }

    @Transactional
    public Word addNewWord(CreateNewWordCommand command){
        if(wordRepo.existsByWord(command.getWord())){
            throw new ApiException(LanguageError.ALREADY_EXISTS,String.format("Word '%s' already exists in database", command.getWord()));
        }
        Language language = getLanguageById(command.getLanguageId());
        Word word = new Word(language, command.getWord(), command.getTranslation());
        saveWord(word);

        return word;
    }

    @Transactional
    public Word saveWord(Word word){
        Long nextWordId = wordRepo.findMaxWordIdByLanguage(word.getLanguage().getId())
                .map(max->max+1)
                .orElse(1L);
        word.setWordId(nextWordId);
        return wordRepo.save(word);
    }

    public Language getLanguageById(Long id) {
        if(!languageRepo.existsById(id)){
            throw new ApiException(LanguageError.LANGUAGE_ID_NOT_FOUND,String.format("Language with id = '%d' doesn't exist", id));
        }
        return languageRepo.findById(id).get();
    }

    public Word getWordByLanguageIdAndWordId(Long id, Long wordId) {
        //if(!wordRepo.existsByLanguageIdAndWordId(id, wordId))
            //throw new ApiException(LanguageError.LANGUAGE_AND_WORD_ID_NOT_FOUND,String.format("Couldn't find word for languageId = %d and wordId = %d",id, wordId));

        Word word = wordRepo.findWordByLanguageIdAndWordId(id,wordId).orElse(null);

        if(word == null){
            throw new ApiException(LanguageError.LANGUAGE_AND_WORD_ID_NOT_FOUND,String.format("Couldn't find word for languageId = %d and wordId = %d",id, wordId));
        }
        return word;
    }

    public List<Word> getWordsList(Long id) {
       return languageRepo.findById(id)
                .map(Language::getWords)
                .orElse(Collections.EMPTY_LIST);
    }

    @Transactional
    public String addCategoryToWord(AddCategoryToWordCommand command) {
        Word word = getWordByLanguageIdAndWordId(command.getLanguageId(),command.getWordId());
        WordCategory category = wordCategoryRepo.findById(command.getCategoryId()).orElse(null);
        if(category == null){
            throw new ApiException(LanguageError.CATEGORY_ID_NOT_FOUND,String.format("Category with id = '%d' doesn't exist",command.getCategoryId()));
        }

        word.AddCategory(category);

        return String.format("Category '%s' added to word '%s'",word.getWord(),category.getName());
    }

    public void addNewCategory(CreateNewCategoryCommand command) {
        if(wordCategoryRepo.existsByName(command.getName())){
            throw new ApiException(LanguageError.ALREADY_EXISTS,String.format("Category '%s' already exists in database", command.getName()));
        }
        WordCategory wordCategory = new WordCategory(command.getName());
        wordCategoryRepo.save(wordCategory);
    }

    public WordCategory getWordCategory(Long categoryId) {
        if(!wordCategoryRepo.existsById(categoryId)){
            throw new ApiException(LanguageError.CATEGORY_ID_NOT_FOUND,String.format("Category with id = '%d' doesn't exist",categoryId));
        }
        return wordCategoryRepo.findById(categoryId).get();
    }

    public Language patchLanguage(Long id, LanguagePatchDTO dto) {
        Language language = languageRepo.findById(id).orElseGet(null);
        if(language == null)
            throw new ApiException(LanguageError.LANGUAGE_ID_NOT_FOUND,String.format("Language with id = '%d' doesn't exist",id));

        if(dto.getName() != null){
            language.setName(dto.getName());
        }
        languageRepo.save(language);
        return language;
    }

    public Word patchWord(Long id, Long wordId, WordPatchDTO dto) {
        Word word = wordRepo.findWordByLanguageIdAndWordId(id,wordId).orElseGet(null);
        if(word == null)
            throw new ApiException(LanguageError.LANGUAGE_AND_WORD_ID_NOT_FOUND,String.format("Couldn't find word for languageId = %d and wordId = %d",id, wordId));

        if(dto.getWord() != null)
            word.setWord(dto.getWord());
        if(dto.getTranslation() != null)
            word.setTranslation(dto.getTranslation());
        if(dto.getReferenceLevel() != null)
            word.setReferenceLevel(dto.getReferenceLevel());
        if(dto.getImportanceLevel() != null)
            word.setImportanceLevel(dto.getImportanceLevel());
        if(dto.getCategoryIds() != null){
            List<WordCategory> categories = wordCategoryRepo.findAllById(dto.getCategoryIds());
            word.setCategories(categories);
        }

        wordRepo.save(word);
        return word;
    }

    public WordCategory patchWordCategory(Long categoryId, WordCategoryPatchDTO dto) {
        WordCategory category = wordCategoryRepo.findById(categoryId).orElseGet(null);
        if(category == null)
            throw new ApiException(LanguageError.CATEGORY_ID_NOT_FOUND,String.format("Category with id = '%d' doesn't exist",categoryId));

        if(dto.getName() != null)
            category.setName(dto.getName());

        wordCategoryRepo.save(category);
        return category;
    }

    @Transactional
    public boolean deleteLanguageById(Long id) {
        if(!languageRepo.existsById(id))
            return false;

        languageRepo.deleteById(id);
        return true;
    }

    @Transactional
    public void deleteAllLanguagesByIds(List<Long> ids) {
        languageRepo.deleteAllById(ids);

    }
    @Transactional
    public boolean deleteWordById(Long id, Long wordId) {
        Word word = wordRepo.findWordByLanguageIdAndWordId(id,wordId).orElseGet(null);
        if(word == null)
            return false;

        wordRepo.deleteById(word.getId());
        return true;
    }
    @Transactional
    public void deleteAllWordsByIds(Long id, List<Long> wordsIds) {
        wordsIds.forEach(wordId -> {
            deleteWordById(id,wordId);
        });
    }

    public boolean deleteCategoryById(Long categoryId) {
        WordCategory wordCategory = wordCategoryRepo.findById(categoryId).orElseGet(null);
        if(wordCategory == null)
            return false;

        wordCategoryRepo.deleteById(wordCategory.getId());
        return true;
    }

    public void deleteAllCategoriesByIds(List<Long> categoryIds) {
        wordCategoryRepo.deleteAllById(categoryIds);
    }
}
