package com.goodquestion.edutrek_server.modules.group.persistence;

import com.goodquestion.edutrek_server.modules.group.key.ComposeWeekdayKey;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "lessons_by_weekday")
@IdClass(ComposeWeekdayKey.class)
public class LessonsByWeekdayEntity {
    @Id
    @Column(name = "group_id")
    private UUID groupId;
    @Id
    @Column(name = "weekday_id")
    private int weekdayId;
}