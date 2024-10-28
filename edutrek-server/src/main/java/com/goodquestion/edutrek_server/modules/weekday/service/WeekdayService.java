package com.goodquestion.edutrek_server.modules.weekday.service;

import com.goodquestion.edutrek_server.modules.weekday.persistence.WeekdayEntity;
import com.goodquestion.edutrek_server.modules.weekday.persistence.WeekdayRepository;
import com.goodquestion.edutrek_server.utility_service.logging.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.goodquestion.edutrek_server.error.ShareException.StatusNotFoundException;


@Service
@RequiredArgsConstructor
public class WeekdayService {

    private final WeekdayRepository repository;

    @Loggable
    public List<WeekdayEntity> getAll() {
        return repository.findAll();
    }

    @Loggable
    public WeekdayEntity getById(int weekdayId) {
        return repository.findById(weekdayId).orElseThrow(() -> new StatusNotFoundException(weekdayId));
    }
}
