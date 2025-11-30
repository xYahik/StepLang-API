package com.example.steplang.repositories.language;

import com.example.steplang.entities.language.WordForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordFormRepository extends JpaRepository<WordForm,Long> {

}
