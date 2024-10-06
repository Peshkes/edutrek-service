package com.goodquestion.edutrek_server.modules.group.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArchiveGroupRepository extends MongoRepository<ArchiveGroupDocument, UUID> {

    // Поиск по courseId и строке поиска (по полю groupName)
    @Query("{'courseId': ?0, 'groupName': { $regex: ?1, $options: 'i' }}")
    Page<ArchiveGroupDocument> findByCourseIdAndSearch(UUID courseId, String search, Pageable pageable);

    // Поиск по courseId
    @Query("{'courseId': ?0}")
    Page<ArchiveGroupDocument> findByCourseId(UUID courseId, Pageable pageable);

    // Поиск по строке (по полю groupName)
    @Query("{'groupName': { $regex: ?0, $options: 'i' }}")
    Page<ArchiveGroupDocument> findBySearch(String search, Pageable pageable);

    // Получение всех архивных групп с пагинацией
    Page<ArchiveGroupDocument> findAll(Pageable pageable);
}