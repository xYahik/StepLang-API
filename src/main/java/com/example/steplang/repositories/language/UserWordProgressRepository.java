package com.example.steplang.repositories.language;

import com.example.steplang.entities.language.UserLanguage;
import com.example.steplang.entities.language.UserWordProgress;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserWordProgressRepository extends JpaRepository<UserWordProgress,Long> {
    @Query("select uwp from UserWordProgress uwp where uwp.wordForm.word.id = ?1 and uwp.userLanguage = ?2")
    Optional<UserWordProgress> findByWordIdAndUserLanguage(Long wordId, UserLanguage userLanguage);

    @Query("select (count(uwp) > 0) from UserWordProgress uwp where uwp.wordForm.word.id = ?1 and uwp.userLanguage = ?2")
    boolean existsByWordIdAndUserLanguage(Long wordId, UserLanguage userLanguage);


    @Query(value = """
    SELECT *
    FROM user_word_progress uwp
    WHERE uwp.user_language_id = :userLanguageId
        AND (
            uwp.next_repetition_date IS NULL
            OR CAST(uwp.next_repetition_date AS DATE) <= CURRENT_DATE
        )
    ORDER BY
        CAST(uwp.next_repetition_date AS DATE) ASC,
        (CASE uwp.understanding_level
            WHEN 'NEW' THEN 0
            WHEN 'LEARNING' THEN 1
            WHEN 'ALMOST_KNOWN' THEN 2
            WHEN 'KNOWN' THEN 3
        END) * 100 + uwp.understanding_progress ASC
    LIMIT 10
    """, nativeQuery = true)
    List<UserWordProgress> find10MostNeededRepetition(@Param("userLanguageId") Long userLanguageId);

    @Query( value = """
            SELECT w.word.baseForm
            FROM UserWordProgress uwp
            JOIN uwp.wordForm w
            WHERE uwp.userLanguage.id = :userLanguageId
            """)
    List<String> findAllWordsByUserLanguage(@Param("userLanguageId")Long id);
}
