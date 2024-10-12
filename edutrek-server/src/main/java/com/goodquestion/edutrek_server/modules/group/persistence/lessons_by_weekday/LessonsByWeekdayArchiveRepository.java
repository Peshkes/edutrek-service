package com.goodquestion.edutrek_server.modules.group.persistence.lessons_by_weekday;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LessonsByWeekdayArchiveRepository extends ILessonsByWeekdayRepository<LessonsByWeekdayArchiveEntity> {
    @Query(value = "DELETE FROM ONLY lessons_by_weekday_archive WHERE group_id = :id", nativeQuery = true)
    void deleteByGroupId(@Param("id") UUID groupId);
    @Query(value = "SELECT * FROM ONLY lessons_by_weekday_archive WHERE group_id = :id", nativeQuery = true)
    List<LessonsByWeekdayArchiveEntity> findByGroupId(@Param("id") UUID uuid);
}
