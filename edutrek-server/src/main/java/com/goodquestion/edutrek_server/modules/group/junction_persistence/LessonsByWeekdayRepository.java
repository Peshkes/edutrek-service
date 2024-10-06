package com.goodquestion.edutrek_server.modules.group.junction_persistence;

import com.goodquestion.edutrek_server.modules.group.key.ComposeWeekdayKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LessonsByWeekdayRepository extends JpaRepository<LessonsByWeekdayEntity, ComposeWeekdayKey> {
    void deleteByGroupId(UUID groupId);
}
