package com.goodquestion.edutrek_server.modules.group.persistence.lessons_and_webinars_by_weekday;

import com.goodquestion.edutrek_server.modules.group.key.ComposeWeekdayKey;
import com.goodquestion.edutrek_server.modules.group.persistence.IJunctionTableRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;
@NoRepositoryBean
public interface ISmthByWeekday<T extends BaseSmthByWeekday> extends IJunctionTableRepository, JpaRepository<T, ComposeWeekdayKey> {
    void deleteByGroupId(@Param("id") UUID groupId);
    List<T> getByGroupId(@Param("id") UUID uuid);
}
