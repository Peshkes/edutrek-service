package com.goodquestion.edutrek_server.modules.group.persistence.webinars_by_weekday;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "webinars_by_weekday_archive")
public class WebinarsByWeekdayArchiveEntity extends WebinarsByWeekdayEntity {
    public WebinarsByWeekdayArchiveEntity(WebinarsByWeekdayEntity webinarsByWeekdayEntity) {
        super(webinarsByWeekdayEntity);
    }

    public WebinarsByWeekdayArchiveEntity (UUID groupId, int weekdayId) {
        super(groupId, weekdayId);
    }
}
