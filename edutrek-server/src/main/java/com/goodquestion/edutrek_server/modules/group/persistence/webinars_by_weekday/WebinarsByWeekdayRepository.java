package com.goodquestion.edutrek_server.modules.group.persistence.webinars_by_weekday;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface WebinarsByWeekdayRepository extends IWebinarsByWeekdayRepository<WebinarsByWeekdayEntity>{
    @Query(value = "DELETE FROM ONLY webinars_by_weekday WHERE group_id = :id", nativeQuery = true)
    void deleteByGroupId(@Param("id") UUID groupId);
    @Query(value = "SELECT * FROM ONLY webinars_by_weekday WHERE group_id = :id", nativeQuery = true)
    List<WebinarsByWeekdayEntity> getByGroupId(@Param("id") UUID uuid);
}
