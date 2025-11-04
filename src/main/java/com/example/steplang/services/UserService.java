package com.example.steplang.services;

import com.example.steplang.commands.AddLanguageToUserCommand;
import com.example.steplang.commands.AddUserLanguageWordCommand;
import com.example.steplang.entities.User;
import com.example.steplang.entities.language.Language;
import com.example.steplang.entities.language.UserLanguage;
import com.example.steplang.entities.language.UserWordProgress;
import com.example.steplang.entities.language.Word;
import com.example.steplang.errors.UserLanguageError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.repositories.UserRepository;
import com.example.steplang.repositories.language.LanguageRepository;
import com.example.steplang.repositories.language.UserLanguageRepository;
import com.example.steplang.repositories.language.UserWordProgressRepository;
import com.example.steplang.repositories.language.WordRepository;
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

        UserLanguage userLanguage = userLanguageRepo.findByLanguageId(languageId).orElse(null);
        if(userLanguage == null)
            throw new ApiException(UserLanguageError.USER_NOT_LEARNING_LANGUAGE,String.format("User with id = '%d' is not learning language with id ='%d'",userId,languageId));

        Word word = wordRepository.findWordByLanguageIdAndWordId(languageId,command.getWordId()).orElse(null);
        if(word == null)
            throw new ApiException(UserLanguageError.LANGUAGE_AND_WORD_ID_NOT_FOUND,String.format("Word with wordId = '%d' and languageId = '%d' not found",command.getWordId(),languageId));

        if(userWordProgressRepo.existsByWordIdAndUserLanguage(command.getWordId(),userLanguage))
            throw new ApiException(UserLanguageError.USER_ALREADY_LEARNING_WORD,String.format("User already learning word with wordId = '%d' and languageId = '%d' not found",command.getWordId(),languageId));

        UnderstandingLevel understandingLevel = command.getUnderstandingLevel();
        Long understandingProgress = command.getUnderstandingProgress();


        UserWordProgress userWordProgress = new UserWordProgress(word,userLanguage, understandingLevel != null ?understandingLevel : UnderstandingLevel.NEW, understandingProgress != null? understandingProgress : 0L);
        userWordProgressRepo.save(userWordProgress);
    }

    public UserWordProgress getUserLanguageWord(Long userId, Long languageId, Long wordId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user == null)
            throw new ApiException(UserLanguageError.USER_NOT_FOUND,String.format("User with id = '%d' not found",userId));

        UserLanguage userLanguage = userLanguageRepo.findByLanguageId(languageId).orElse(null);
        if(userLanguage == null)
            throw new ApiException(UserLanguageError.USER_NOT_LEARNING_LANGUAGE,String.format("User withId = '%d' is not learning language with languageId ='%d'",userId,languageId));

        UserWordProgress userWordProgress = userWordProgressRepo.findByWordIdAndUserLanguage(wordId,userLanguage).orElse(null);

        if(userWordProgress == null)
            throw new ApiException(UserLanguageError.USER_NOT_LEARNING_WORD,String.format("User is not learning word withId ='%d' and languageId = '%d'",wordId,languageId));

        return userWordProgress;
    }
}
