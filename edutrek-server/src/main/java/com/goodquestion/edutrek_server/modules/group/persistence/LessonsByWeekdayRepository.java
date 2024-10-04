package com.goodquestion.edutrek_server.modules.group.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonsByWeekdayRepository extends JpaRepository<LessonsByWeekday, ComposeWeekdayKey> { }
