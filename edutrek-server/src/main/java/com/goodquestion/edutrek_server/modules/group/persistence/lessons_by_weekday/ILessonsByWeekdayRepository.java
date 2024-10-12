package com.goodquestion.edutrek_server.modules.group.persistence.lessons_by_weekday;

import com.goodquestion.edutrek_server.modules.group.persistence.IJunctionTableRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ILessonsByWeekdayRepository<T extends LessonsByWeekdayEntity> extends IJunctionTableRepository, JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {
    void deleteByGroupId(UUID groupId);
    List<T> getByGroupId(UUID uuid);
}