package com.example.steplang.repositories.language;

import com.example.steplang.entities.language.WordCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordCategoryRepository extends JpaRepository<WordCategory,Long> {
    Optional<WordCategory> findById(Long id);
    boolean existsByName(String name);
}
