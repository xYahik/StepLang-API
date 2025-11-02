package com.example.steplang.repositories.language;

import com.example.steplang.entities.language.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word,Long> {
    boolean existsByWord(String word);
    List<Word> findByLanguageId(Long id);

    @Query("SELECT MAX(w.wordId) FROM Word w WHERE w.language.id = :languageId")
    Optional<Long> findMaxWordIdByLanguage(@Param("languageId") Long languageId);

    @Query("SELECT w FROM Word w where w.language.id = :languageId AND w.wordId = :wordId")
    Optional<Word> findWordByLanguageIdAndWordId(@Param("languageId") Long languageId, @Param("wordId") Long wordId);

    @Query("SELECT COUNT(w) > 0 FROM Word w where w.language.id = :languageId AND w.wordId = :wordId")
    boolean existsByLanguageIdAndWordId(@Param("languageId")Long languageId, @Param("wordId") Long wordId);
}
