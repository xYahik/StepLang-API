package com.example.steplang.controllers;

import com.example.steplang.services.quest.QuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quest")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUserQuests(){
        return ResponseEntity.ok("");
    }
}
