package com.example.steplang.repositories.language;

import com.example.steplang.entities.language.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,Long> {

    @Query("""
            select (count(c) > 0) from Course c
            where c.name = ?1 and c.learningLanguage.id = ?2 and c.nativeLanguage.id = ?3""")
    boolean existsByNameAndLanguages(String name, Long learningLanguageId, Long nativeLanguageId);

    @Query("select c from Course c where c.learningLanguage.id = ?1 and c.nativeLanguage.id = ?2")
    List<Course> findByLearningLanguageIdAndNativeLanguageId(Long learningLanguageId, Long nativeLanguageId);

}
