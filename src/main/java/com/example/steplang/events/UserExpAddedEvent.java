package com.example.steplang.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserExpAddedEvent {
    private final Long userId;
    private final Long languageId;
    private final Long exp;
}
