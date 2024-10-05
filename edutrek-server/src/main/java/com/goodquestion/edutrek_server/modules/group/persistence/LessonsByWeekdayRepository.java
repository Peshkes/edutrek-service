package com.goodquestion.edutrek_server.modules.group.persistence;

import com.goodquestion.edutrek_server.modules.group.key.ComposeWeekdayKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonsByWeekdayRepository extends JpaRepository<LessonsByWeekdayEntity, ComposeWeekdayKey> { }
