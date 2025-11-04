package com.example.steplang.repositories.language;

import com.example.steplang.entities.language.UserLanguage;
import com.example.steplang.entities.language.UserWordProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWordProgressRepository extends JpaRepository<UserWordProgress,Long> {
    @Query("select uwp from UserWordProgress uwp where uwp.word.id = ?1 and uwp.userLanguage = ?2")
    Optional<UserWordProgress> findByWordIdAndUserLanguage(Long wordId, UserLanguage userLanguage);

    @Query("select (count(uwp) > 0) from UserWordProgress uwp where uwp.word.id = ?1 and uwp.userLanguage = ?2")
    boolean existsByWordIdAndUserLanguage(Long wordId, UserLanguage userLanguage);


}
