package com.goodquestion.edutrek_server.modules.group.persistence.webinars_by_weekday;

import com.goodquestion.edutrek_server.modules.group.key.ComposeWeekdayKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "webinars_by_weekday")
@IdClass(ComposeWeekdayKey.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class WebinarsByWeekdayEntity {
    @Id
    @Column(name = "group_id")
    private UUID groupId;
    @Id
    @Column(name = "weekday_id")
    private int weekdayId;

    public WebinarsByWeekdayEntity(WebinarsByWeekdayEntity webinarsByWeekdayEntity) {
        this.groupId = webinarsByWeekdayEntity.getGroupId();
        this.weekdayId = webinarsByWeekdayEntity.getWeekdayId();
    }
}
