package com.goodquestion.edutrek_server.modules.weekday.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "weekdays")
@NoArgsConstructor
public class WeekdayEntity {
    @Id
    @Column(name = "weekday_id")
    private int weekdayId;
    @Column(name = "weekday_name")
    private String weekdayName;

    public WeekdayEntity(String weekdayName) {
        this.weekdayName = weekdayName;
    }
}