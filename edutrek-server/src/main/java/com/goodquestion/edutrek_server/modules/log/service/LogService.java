package com.goodquestion.edutrek_server.modules.log.service;

import com.goodquestion.edutrek_server.error.ShareException;
import com.goodquestion.edutrek_server.modules.log.persistence.LogDocument;
import com.goodquestion.edutrek_server.modules.log.persistence.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository repository;

    public void add(UUID id, String log) {
        LogDocument document = repository.findById(id)
                .orElse(new LogDocument(id));
        List<String> logs = new ArrayList<>(document.getLogs());
        logs.add(LocalDate.now() + " – " + log);
        document.setLogs(logs);
        repository.save(document);
    }

    public List<String> getById(UUID uuid) {
        return repository.findById(uuid)
                .orElseThrow(() -> new ShareException.LogNotFoundException(uuid.toString()))
                .getLogs();
    }
}