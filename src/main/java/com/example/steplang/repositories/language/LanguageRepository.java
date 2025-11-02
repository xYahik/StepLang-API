package com.example.steplang.repositories.language;

import com.example.steplang.entities.language.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language,Long> {
    Optional<Language> findById(Long id);
    boolean existsByName(String name);
    boolean existsById(Long id);
}
