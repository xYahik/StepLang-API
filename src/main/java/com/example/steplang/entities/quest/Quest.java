package com.example.steplang.entities.quest;

import com.example.steplang.model.quest.QuestData;
import com.example.steplang.utils.enums.quest.QuestActionType;
import com.example.steplang.utils.enums.quest.QuestIntervalType;
import com.example.steplang.utils.enums.quest.QuestStatus;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.time.Instant;

@Entity
@Data
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Long userId;
    private QuestActionType type;
    private QuestIntervalType intervalType;
    private QuestStatus status;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private QuestData data;

    private Instant validUntil;

}
