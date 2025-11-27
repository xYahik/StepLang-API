package com.example.steplang.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskCompletedEvent {
    private String taskId;
}
