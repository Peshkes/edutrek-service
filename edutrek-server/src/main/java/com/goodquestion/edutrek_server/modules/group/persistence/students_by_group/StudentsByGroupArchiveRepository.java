package com.goodquestion.edutrek_server.modules.group.persistence.students_by_group;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentsByGroupArchiveRepository extends IStudentsByGroupRepository<StudentsByGroupArchiveEntity> {
    @Query(value = "SELECT CASE WHEN count(*) > 0 THEN true ELSE false END FROM ONLY students_by_group_archive WHERE group_id = :groupId AND student_num = :studentId", nativeQuery = true)
    boolean existsByGroupIdAndStudentId(UUID groupId, UUID studentId);

    @Query(value = "DELETE FROM ONLY students_by_group_archive WHERE group_id = :id", nativeQuery = true)
    void deleteByGroupId(@Param("id") UUID groupId);

    @Query(value = "SELECT * FROM students_by_group_archive WHERE group_id  = :id", nativeQuery = true)
    List<StudentsByGroupArchiveEntity> getByGroupId(@Param("id") UUID uuid);

    @Query(value = "SELECT * FROM students_by_group_archive WHERE student_num = :studentId AND group_id = :groupId", nativeQuery = true)
    Optional<StudentsByGroupArchiveEntity> getByGroupIdAndStudentId(@Param("groupId") UUID groupId, @Param("studentId") UUID studentId);
}
