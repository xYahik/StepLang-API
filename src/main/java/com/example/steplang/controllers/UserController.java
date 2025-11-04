package com.example.steplang.controllers;

import com.example.steplang.commands.AddLanguageToUserCommand;
import com.example.steplang.commands.AddUserLanguageWordCommand;
import com.example.steplang.dtos.user.UserAuthInfoDTO;
import com.example.steplang.dtos.user.UserLanguageWordDTO;
import com.example.steplang.dtos.user.UserMeDTO;
import com.example.steplang.dtos.user.UserProfileDTO;
import com.example.steplang.entities.User;
import com.example.steplang.entities.language.UserWordProgress;
import com.example.steplang.mappers.UserMapper;
import com.example.steplang.repositories.UserRepository;
import com.example.steplang.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.steplang.utils.JwtUtil;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepo;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;


    @GetMapping("/me")
    public ResponseEntity<?> getMe(){
        String email = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserAuthInfoDTO user) {
                email = user.getEmail();
            }
        }
        User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        UserMeDTO dto = userMapper.toMeDto(user);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable Long id){
        User user = userRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));
        UserProfileDTO dto = userMapper.toProfileDto(user);
        return ResponseEntity.ok(dto);
    }

    //api/user/language/add (post dodanie jezyka uzytkownikowi)
    //api/user/language/{languageId}/word/add (post dodanie slowka uzytkownikowi)
    //api/user/language/{languageId}/{wordId} (get  slowka i progressu?)
    @PostMapping("/language/add")
    public ResponseEntity<?> addLanguageToUser(Authentication authentication, @Valid @RequestBody AddLanguageToUserCommand command){
        Long userId = null;
        if(authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserAuthInfoDTO user) {
                userId = user.getId();
            }
        }

        userService.addLanguageToUser(userId,command);
        return ResponseEntity.ok("");
    }

    @PostMapping("/language/{languageId}/word/add")
    public ResponseEntity<?> addUserLanguageWord(@PathVariable Long languageId, @Valid @RequestBody AddUserLanguageWordCommand command){
        Long userId = jwtUtil.getUserAuthInfo().getId();
        userService.addUserLanguageWord(userId,languageId,command);
        return ResponseEntity.ok("");
    }

    @GetMapping("/language/{languageId}/{wordId}")
    public ResponseEntity<?> getUserLanguageWordInformation(@PathVariable Long languageId, @PathVariable Long wordId){
        Long userId = jwtUtil.getUserAuthInfo().getId();
        UserWordProgress userWordProgress = userService.getUserLanguageWord(userId,languageId,wordId);

        UserLanguageWordDTO dto = userMapper.toUserLanguageWordDto(userWordProgress);
        return ResponseEntity.ok(dto);
    }
}
