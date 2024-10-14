package com.goodquestion.edutrek_server.modules.group.persistence.groups;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface IGroupRepository<T extends BaseGroup> extends JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {
    boolean checkGroupExistsById(UUID id);
    Optional<BaseGroup> getGroupByGroupId(UUID id);
    Page<T> findAll(Specification<T> spec, Pageable pageable);
    void deleteGroupByGroupId(UUID id);
}
