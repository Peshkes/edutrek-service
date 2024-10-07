package com.goodquestion.edutrek_server.modules.weekday.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekdayRepository extends JpaRepository<WeekdayEntity, Integer> {

}