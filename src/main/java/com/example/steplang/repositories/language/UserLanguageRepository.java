package com.example.steplang.repositories.language;

import com.example.steplang.entities.User;
import com.example.steplang.entities.language.Language;
import com.example.steplang.entities.language.UserLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLanguageRepository extends JpaRepository<UserLanguage,Long> {

    @Query("select (count(ul) > 0) from UserLanguage ul where ul.user.id = ?1 and ul.language.id = ?2")
    boolean existsByUserIdAndLanguageId(Long userId, Long languageId);

    @Query("select ul from UserLanguage ul where ul.language.id = ?1")
    Optional<UserLanguage> findByLanguageId(Long id);

}
