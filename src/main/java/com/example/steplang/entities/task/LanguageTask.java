package com.example.steplang.entities.task;

import com.example.steplang.utils.enums.LanguageTaskType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@RedisHash(value = "languageTask", timeToLive = 3600)
public class LanguageTask {
    @Id
    private String taskId;

    @Indexed
    private Long userId;
    @Indexed
    private Long languageId;

    private LanguageTaskType languageTaskType;

    private TaskDataBase taskData;
}
