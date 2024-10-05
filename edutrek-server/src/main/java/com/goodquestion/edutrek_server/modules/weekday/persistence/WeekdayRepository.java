package com.goodquestion.edutrek_server.modules.weekday.persistence;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheConfig(cacheNames = "weekdays")
public interface WeekdayRepository extends JpaRepository<WeekdayEntity, Integer> {
    @Cacheable(key = "#root.methodName")
    List<WeekdayEntity> getAll();
}