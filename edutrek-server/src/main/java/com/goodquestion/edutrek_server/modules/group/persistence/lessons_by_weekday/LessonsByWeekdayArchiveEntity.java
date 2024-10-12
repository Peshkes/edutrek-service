package com.goodquestion.edutrek_server.modules.group.persistence.lessons_by_weekday;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "lessons_by_weekday_archive")
public class LessonsByWeekdayArchiveEntity extends LessonsByWeekdayEntity {
    public LessonsByWeekdayArchiveEntity(LessonsByWeekdayEntity lessonsByWeekdayEntity) {
        super(lessonsByWeekdayEntity);
    }

    public LessonsByWeekdayArchiveEntity (UUID groupId, int weekdayId) {
        super(groupId, weekdayId);
    }
}
