package com.goodquestion.edutrek_server.modules.group.persistence.webinars_by_weekday;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface WebinarsByWeekdayArchiveRepository extends IWebinarsByWeekdayRepository<WebinarsByWeekdayArchiveEntity>  {
    @Query(value = "DELETE FROM ONLY webinars_by_weekday_archive WHERE group_id = :id", nativeQuery = true)
    void deleteByGroupId(@Param("id") UUID groupId);
    @Query(value = "SELECT * FROM ONLY webinars_by_weekday_archive WHERE group_id = :id", nativeQuery = true)
    List<WebinarsByWeekdayArchiveEntity> getByGroupId(@Param("id") UUID uuid);
}
