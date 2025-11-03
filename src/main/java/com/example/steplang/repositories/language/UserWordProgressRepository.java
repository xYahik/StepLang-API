package com.example.steplang.repositories.language;

import com.example.steplang.entities.language.UserWordProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWordProgressRepository extends JpaRepository<UserWordProgress,Long> {
}
