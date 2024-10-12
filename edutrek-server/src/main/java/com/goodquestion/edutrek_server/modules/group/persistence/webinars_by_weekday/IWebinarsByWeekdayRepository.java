package com.goodquestion.edutrek_server.modules.group.persistence.webinars_by_weekday;

import com.goodquestion.edutrek_server.modules.group.key.ComposeWeekdayKey;
import com.goodquestion.edutrek_server.modules.group.persistence.IJunctionTableRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IWebinarsByWeekdayRepository<T extends WebinarsByWeekdayEntity> extends IJunctionTableRepository, JpaRepository<T, ComposeWeekdayKey> {
   void deleteByGroupId(UUID groupId);
   List<T> getByGroupId(UUID uuid);
}
