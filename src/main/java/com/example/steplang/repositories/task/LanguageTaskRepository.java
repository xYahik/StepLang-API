package com.example.steplang.repositories.task;

import com.example.steplang.entities.task.LanguageTask;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LanguageTaskRepository extends CrudRepository<LanguageTask,String> {
    Optional<LanguageTask> findByUserId(Long userId);
}
