package com.goodquestion.edutrek_server.modules.group.persistence.groups;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface IGroupRepository<T extends GroupEntity> {
    boolean checkGroupExistsById(UUID id);
    Optional<T> getGroupByGroupId(UUID id);
    Page<T> findAll(Specification<T> spec, Pageable pageable);
    void deleteGroupByGroupId(UUID id);
}
