package com.example.steplang.repositories.language;

import com.example.steplang.entities.language.UserLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLanguageRepository extends JpaRepository<UserLanguage,Long> {
}
