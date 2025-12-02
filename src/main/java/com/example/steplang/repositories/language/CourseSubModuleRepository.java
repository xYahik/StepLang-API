package com.example.steplang.repositories.language;

import com.example.steplang.entities.language.CourseModule;
import com.example.steplang.entities.language.CourseSubModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CourseSubModuleRepository extends JpaRepository<CourseSubModule,Long> {
    @Query("SELECT (count(s) > 0) FROM CourseSubModule s WHERE s.subModuleId = ?1 AND s.module = ?2")
    boolean existsBySubModuleIdAndModule(Integer subModuleId, CourseModule module);

    Optional<CourseSubModule> findBySubModuleIdAndModule(Integer subModuleId, CourseModule module);
}
