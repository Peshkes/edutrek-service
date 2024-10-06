package com.goodquestion.edutrek_server.modules.group.junction_persistence;

import com.goodquestion.edutrek_server.modules.group.key.ComposeWeekdayKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WebinarsByWeekdayRepository extends JpaRepository<WebinarsByWeekdayEntity, ComposeWeekdayKey> {
    void deleteByGroupId(UUID groupId);
}
