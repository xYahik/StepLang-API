package com.example.steplang.repositories.language;

import com.example.steplang.entities.language.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CourseModuleRepository extends JpaRepository<CourseModule,Long> {

    @Query("SELECT (count(m) >0) from CourseModule m WHERE m.moduleId = ?1 AND m.course.id = ?2")
    boolean existsByModuleIdAndCourseId(Integer moduleId, Long courseId);
    @Query("SELECT m from CourseModule m WHERE m.moduleId = ?1 AND m.course.id = ?2")
    Optional<CourseModule> findByModuleIdAndCourseId(Integer moduleId, Long courseId);
}
