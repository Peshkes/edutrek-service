package com.goodquestion.edutrek_server.modules.group.persistence.lessons_by_weekday;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LessonsByWeekdayRepository extends ILessonsByWeekdayRepository<LessonsByWeekdayEntity> {
    @Query(value = "DELETE FROM ONLY lessons_by_weekday WHERE group_id = :id", nativeQuery = true)
    void deleteByGroupId(@Param("id") UUID groupId);
    @Query(value = "SELECT * FROM ONLY lessons_by_weekday WHERE group_id = :id", nativeQuery = true)
    List<LessonsByWeekdayEntity> findByGroupId(@Param("id") UUID uuid);
}
