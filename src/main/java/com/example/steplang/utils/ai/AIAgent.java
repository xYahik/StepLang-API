package com.example.steplang.utils.ai;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AIAgent {
    @Autowired
    private ChatModel chatModel;
    public String generateAITask(String prompt) {
        String aiResponse = chatModel.call(prompt);
        return aiResponse;
    }
}
