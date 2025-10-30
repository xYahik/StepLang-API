package com.example.steplang.controllers;

import com.example.steplang.dtos.user.UserMeDTO;
import com.example.steplang.dtos.user.UserProfileDTO;
import com.example.steplang.entities.User;
import com.example.steplang.mappers.UserMapper;
import com.example.steplang.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.steplang.utils.JwtUtil;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    public UserController(UserRepository userRepo, JwtUtil jwtUtil,UserMapper userMapper){
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(){
        String email = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof User user) {
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
}
