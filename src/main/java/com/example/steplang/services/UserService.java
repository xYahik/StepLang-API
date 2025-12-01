package com.example.steplang.services;

import com.example.steplang.commands.AddLanguageToUserCommand;
import com.example.steplang.commands.AddUserLanguageWordCommand;
import com.example.steplang.entities.User;
import com.example.steplang.entities.language.*;
import com.example.steplang.errors.LanguageError;
import com.example.steplang.errors.UserLanguageError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.repositories.UserRepository;
import com.example.steplang.repositories.language.*;
import com.example.steplang.utils.enums.UnderstandingLevel;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final UserLanguageRepository userLanguageRepo;
    private final UserWordProgressRepository userWordProgressRepo;

    private final LanguageRepository languageRepo;
    private final WordRepository wordRepository;
    private final WordFormRepository wordFormRepo;

    @Transactional
    public User register(String username, String email, String password){
        if(userRepo.existsByUsername(username))
            throw new RuntimeException("UserName already used");
        if(userRepo.existsByEmail(email))
            throw new RuntimeException("Email already used");

        String hashedPassword = encoder.encode(password);
        User newUser = new User(username,email,hashedPassword);

        return userRepo.save(newUser);
    }

    @Transactional
    public void addLanguageToUser(Long userId, AddLanguageToUserCommand command){
        User user = userRepo.findById(userId).orElse(null);
        if(user == null)
            throw new ApiException(UserLanguageError.USER_NOT_FOUND,String.format("User with id = '%d' not found",userId));
        Language language = languageRepo.findById(command.getLanguageId()).orElse(null);
        if(language == null)
            throw new ApiException(UserLanguageError.LANGUAGE_NOT_FOUND,String.format("Language with id = '%d' not found",command.getLanguageId()));

        if(userLanguageRepo.existsByUserIdAndLanguageId(userId, command.getLanguageId()))
            throw new ApiException(UserLanguageError.USER_ALREADY_LEARNING_LANGUAGE,"User already learning this language");

        UserLanguage userLanguage = new UserLanguage(user,language);
        userLanguageRepo.save(userLanguage);
    }

    public void addUserLanguageWord(Long userId,Long languageId, AddUserLanguageWordCommand command) {
        User user = userRepo.findById(userId).orElse(null);
        if(user == null)
            throw new ApiException(UserLanguageError.USER_NOT_FOUND,String.format("User with id = '%d' not found",userId));

        UserLanguage userLanguage = userLanguageRepo.findByLanguageIdAndUserId(languageId,userId).orElse(null);
        if(userLanguage == null)
            throw new ApiException(UserLanguageError.USER_NOT_LEARNING_LANGUAGE,String.format("User with id = '%d' is not learning language with id ='%d'",userId,languageId));

        //Word word = wordRepository.findWordByLanguageIdAndWordId(languageId,command.getWordId()).orElse(null);
        //if(word == null)
        //    throw new ApiException(UserLanguageError.LANGUAGE_AND_WORD_ID_NOT_FOUND,String.format("Word with wordId = '%d' and languageId = '%d' not found",command.getWordId(),languageId));

        WordForm wordForm = wordFormRepo.findById(command.getWordFormId()).orElse(null);
        if(wordForm == null){
            throw new ApiException(LanguageError.WORD_FORM_ID_NOT_FOUND,String.format("Couldn't find wordForm with formId = '%d'", command.getWordFormId()));
        }

        if(userWordProgressRepo.existsByWordIdAndUserLanguage(command.getWordFormId(),userLanguage))
            throw new ApiException(UserLanguageError.USER_ALREADY_LEARNING_WORD,String.format("User already learning word with wordFormId = '%d' and languageId = '%d' not found",command.getWordFormId(),languageId));

        UnderstandingLevel understandingLevel = command.getUnderstandingLevel();
        Long understandingProgress = command.getUnderstandingProgress();


        UserWordProgress userWordProgress = new UserWordProgress(wordForm,userLanguage, understandingLevel != null ?understandingLevel : UnderstandingLevel.NEW, understandingProgress != null? understandingProgress : 0L);
        userWordProgressRepo.save(userWordProgress);
    }

    public UserWordProgress getUserLanguageWordForm(Long userId, Long languageId,Long wordId, Long wordFormId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user == null)
            throw new ApiException(UserLanguageError.USER_NOT_FOUND,String.format("User with id = '%d' not found",userId));

        UserLanguage userLanguage = userLanguageRepo.findByLanguageIdAndUserId(languageId,userId).orElse(null);
        if(userLanguage == null)
            throw new ApiException(UserLanguageError.USER_NOT_LEARNING_LANGUAGE,String.format("User withId = '%d' is not learning language with languageId ='%d'",userId,languageId));

        Word word = wordRepository.findWordByLanguageIdAndWordId(languageId,wordId).orElse(null);
        if(word == null)
            throw new ApiException(LanguageError.WORD_ID_NOT_FOUND, String.format("Couldn't find word with id ='%d' for language with id = '%d",wordId,languageId));

        if(word.getForms().stream().filter(f-> f.getId() == wordFormId).findFirst().orElse(null) == null){
            throw new ApiException(LanguageError.WORD_FORM_ID_NOT_FOUND, String.format("Couldn't find form with id = '%d' for word with id ='%d",wordFormId,wordId));
        }

        UserWordProgress userWordProgress = userWordProgressRepo.findByWordFormIdAndUserLanguage(wordFormId,userLanguage).orElse(null);

        if(userWordProgress == null)
            throw new ApiException(UserLanguageError.USER_NOT_LEARNING_WORD,String.format("User is not learning word in form with wordFormId ='%d' and languageId = '%d'",wordFormId,languageId));

        return userWordProgress;
    }
}
