package com.example.steplang.repositories.quest;

import com.example.steplang.entities.quest.Quest;
import com.example.steplang.utils.enums.quest.QuestActionType;
import com.example.steplang.utils.enums.quest.QuestIntervalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest,Long> {
    @Transactional
    @Modifying
    @Query("DELETE from Quest q WHERE q.validUntil < CURRENT_TIMESTAMP OR q.validUntil IS NULL")
    void deleteExpiredQuests();

    @Query("select q from Quest q where q.userId = ?1 AND CURRENT_TIMESTAMP >= q.validUntil")
    List<Quest> findOldQuestsByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE from Quest q WHERE q.userId = ?1 AND CURRENT_TIMESTAMP >= q.validUntil")
    void deleteExpiredQuestsByUserId(Long userId);

    @Query("select q from Quest q where q.userId = ?1 and q.type = ?2")
    List<Quest> findByUserIdAndType(Long userId, QuestActionType type);

    @Query("select q from Quest q where q.userId = ?1 and q.intervalType = ?2")
    List<Quest> findByUserIdAndIntervalType(Long userId, QuestIntervalType intervalType);


}
