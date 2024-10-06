package com.goodquestion.edutrek_server.modules.log.service;

import com.goodquestion.edutrek_server.error.ShareException;
import com.goodquestion.edutrek_server.modules.log.persistence.LogDocument;
import com.goodquestion.edutrek_server.modules.log.persistence.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogService {

    LogRepository repository;

    public void add(UUID id, String log) {
        LogDocument document = repository.findById(id)
                .orElse(new LogDocument(id));
        document.getLogs().add(LocalDate.now() + " – " + log);
        repository.save(document);
    }

    public List<String> getById(UUID uuid) {
        return repository.findById(uuid)
                .orElseThrow(() -> new ShareException.LogNotFoundException(uuid.toString()))
                .getLogs();
    }
}