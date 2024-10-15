package com.goodquestion.edutrek_server.modules.group.persistence.lecturers_by_group;

import com.goodquestion.edutrek_server.modules.group.key.ComposeLecturerKey;
import com.goodquestion.edutrek_server.modules.group.persistence.IJunctionTableRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.UUID;

@NoRepositoryBean
public interface ILecturerByGroupRepository<T extends BaseLecturerByGroup> extends IJunctionTableRepository, JpaRepository<T, ComposeLecturerKey> {
    void deleteByGroupId(UUID groupId);
    List<T> getByGroupId(UUID uuid);
}