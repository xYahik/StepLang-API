package com.example.steplang.repositories;

import com.example.steplang.model.LevelingLog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LevelingLogRepository extends CrudRepository<LevelingLog, String> {
    List<LevelingLog> findByUserIdAndLanguageIdOrderByDateDesc(Long userId, Long languageId);
}
