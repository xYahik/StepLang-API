package com.example.steplang.controllers;

import com.example.steplang.dtos.quest.QuestDTO;
import com.example.steplang.entities.quest.Quest;
import com.example.steplang.mappers.QuestMapper;
import com.example.steplang.services.quest.QuestService;
import com.example.steplang.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quest")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;
    private final JwtUtil jwtUtil;
    private final QuestMapper questMapper;
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUserQuests(){
        Long userId = jwtUtil.getUserAuthInfo().getId();
        List<Quest> quests = questService.getUserQuests(userId);
        List<QuestDTO> questDTOs = questMapper.toDtoList(quests);
        return ResponseEntity.ok(questDTOs);
    }
}
