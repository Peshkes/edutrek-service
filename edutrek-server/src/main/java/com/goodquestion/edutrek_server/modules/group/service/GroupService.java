package com.goodquestion.edutrek_server.modules.group.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;
import com.goodquestion.edutrek_server.error.ShareException.CourseNotFoundException;
import com.goodquestion.edutrek_server.error.ShareException.GroupNotFoundException;
import com.goodquestion.edutrek_server.error.ShareException.StudentAlreadyInThisGroupException;
import com.goodquestion.edutrek_server.error.ShareException.StudentNotFoundInThisGroupException;
import com.goodquestion.edutrek_server.modules.group.persistence.ArchiveGroupDocument;
import com.goodquestion.edutrek_server.modules.group.persistence.ArchiveGroupRepository;
import com.goodquestion.edutrek_server.modules.group.dto.*;
import com.goodquestion.edutrek_server.modules.group.junction_persistence.*;
import com.goodquestion.edutrek_server.modules.group.key.ComposeStudentsKey;
import com.goodquestion.edutrek_server.modules.group.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ArchiveGroupRepository archiveGroupRepository;
    private final LecturersByGroupRepository lecturersByGroupRepository;
    private final StudentsByGroupRepository studentsByGroupRepository;
    private final LessonsByWeekdayRepository lessonsByWeekdayRepository;
    private final WebinarsByWeekdayRepository webinarsByWeekdayRepository;

    public List<GroupDto> getAll() {
        List<GroupEntity> groupEntities = groupRepository.findAll();
        if (!groupEntities.isEmpty()) {
            return groupEntities.stream().map(this::constructDto).toList();
        } else
            return List.of();
    }

    public GroupDto getById(UUID groupId) {
        return constructDto(groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(String.valueOf(groupId))));
    }

    @Transactional
    public void addEntity(AddGroupDto groupData) {
        try {
            groupRepository.save(constructEntity(groupData));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Transactional
    public void deleteById(UUID groupId) {
        if (!groupRepository.existsById(groupId))
            throw new CourseNotFoundException(String.valueOf(groupId));

        try {
            groupRepository.deleteById(groupId);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    public void updateById(UUID groupId, UpdateGroupDto groupData) {
        GroupEntity groupEntity = groupRepository.findById(groupId).orElseThrow(() -> new CourseNotFoundException(String.valueOf(groupId)));

        if (groupData.getGroupName() != null) groupEntity.setGroupName(groupData.getGroupName());
        if (groupData.getFinishDate() != null) groupEntity.setFinishDate(groupData.getFinishDate());
        if (groupData.getIsActive() != null) groupEntity.setIsActive(groupData.getIsActive());
        if (groupData.getCourseId() != null) groupEntity.setCourseId(groupData.getCourseId());
        if (groupData.getSlackLink() != null) groupEntity.setSlackLink(groupData.getSlackLink());
        if (groupData.getWhatsAppLink() != null) groupEntity.setWhatsAppLink(groupData.getWhatsAppLink());
        if (groupData.getSkypeLink() != null) groupEntity.setSkypeLink(groupData.getSkypeLink());
        if (groupData.getDeactivateAfter() != null) groupEntity.setDeactivateAfter(groupData.getDeactivateAfter());

        if (groupData.getLessons() != null) {
            lessonsByWeekdayRepository.deleteByGroupId(groupId);
            for (int weekdayId : groupData.getLessons()) {
                try {
                    lessonsByWeekdayRepository.save(new LessonsByWeekdayEntity(groupId, weekdayId));
                } catch (Exception e) {
                    throw new DatabaseAddingException(e.getMessage());
                }
            }
        }

        if (groupData.getWebinars() != null) {
            webinarsByWeekdayRepository.deleteByGroupId(groupId);
            for (int weekdayId : groupData.getLessons()) {
                try {
                    lessonsByWeekdayRepository.save(new LessonsByWeekdayEntity(groupId, weekdayId));
                } catch (Exception e) {
                    throw new DatabaseAddingException(e.getMessage());
                }
            }
        }

        try {
            groupRepository.save(groupEntity);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }

    }

    public PaginationResponse getAllPaginated(int page, int size, String courseId, Boolean isActive, String search) {
        Pageable pageable = PageRequest.of(page, size);

        List<GroupDto> groups;
        long totalGroups;

        if (Boolean.TRUE.equals(isActive)) {
            Page<GroupEntity> activeGroups = getActiveGroups(pageable, courseId, search);
            groups = activeGroups.getContent().stream().map(this::constructDto).toList();
            totalGroups = activeGroups.getTotalElements();
        } else if (Boolean.FALSE.equals(isActive)) {
            Page<ArchiveGroupDocument> archiveGroups = getArchiveGroups(pageable, courseId, search);
            groups = archiveGroups.getContent().stream().map(this::constructDto).toList();
            totalGroups = archiveGroups.getTotalElements();
        } else {
            Page<GroupEntity> activeGroups = getActiveGroups(pageable, courseId, search);
            groups = new ArrayList<>(activeGroups.getContent().stream().map(this::constructDto).toList());
            totalGroups = activeGroups.getTotalElements();

            if (groups.size() < size) {
                int remainingSize = size - groups.size();
                int archivePage = (page * size + groups.size()) / size;
                Pageable archivePageable = PageRequest.of(archivePage, remainingSize);

                Page<ArchiveGroupDocument> archiveGroups = getArchiveGroups(archivePageable, courseId, search);
                List<GroupDto> archiveGroupDtos = archiveGroups.getContent().stream().map(this::constructDto).toList();

                groups.addAll(archiveGroupDtos);
                totalGroups += archiveGroups.getTotalElements();
            }
        }

        return new PaginationResponse(groups, totalGroups, page, size);
    }

    public void addStudentsToGroup(UUID uuid, List<UUID> students) {
        if (groupRepository.existsById(uuid)) {
            for (UUID student : students) {
                if (studentsByGroupRepository.existsByGroupIdAndStudentId(uuid, student))
                    try {
                        studentsByGroupRepository.save(new StudentsByGroupEntity(uuid, student, true));
                    } catch (Exception e) {
                        throw new DatabaseAddingException(e.getMessage());
                    }
            }
        } else
            throw new GroupNotFoundException(String.valueOf(uuid));
    }

    @Transactional
    public void moveStudentsBetweenGroups(UUID fromId, UUID toId, List<UUID> students) {
        if (groupRepository.existsById(fromId)) {
            if (groupRepository.existsById(toId)) {
                for (UUID student : students) {
                    if (studentsByGroupRepository.existsByGroupIdAndStudentId(toId, student)) {
                        StudentsByGroupEntity studentsByGroupEntity = studentsByGroupRepository.findById(new ComposeStudentsKey(fromId, student)).orElseThrow(() -> new StudentNotFoundInThisGroupException(fromId.toString(), student.toString()));
                        try {
                            studentsByGroupRepository.save(new StudentsByGroupEntity(toId, student, true));
                        } catch (Exception e) {
                            throw new DatabaseAddingException(e.getMessage());
                        }
                        studentsByGroupEntity.setIsActive(false);
                    } else
                        throw new StudentAlreadyInThisGroupException(toId.toString(), student.toString());
                }
            } else
                throw new GroupNotFoundException(String.valueOf(toId));
        } else
            throw new GroupNotFoundException(String.valueOf(fromId));
    }

    @Transactional
    public void changeLecturersToGroup(UUID uuid, List<ChangeLecturersDto> changeLecturers) {
        if (groupRepository.existsById(uuid)) {
            lecturersByGroupRepository.deleteByGroupId(uuid);
            for (ChangeLecturersDto changeLecturer : changeLecturers) {
                try {
                    lecturersByGroupRepository.save(new LecturersByGroupEntity(uuid, changeLecturer.getLecturerId(), changeLecturer.getIsWebinarist()));
                } catch (Exception e) {
                    throw new DatabaseAddingException(e.getMessage());
                }
            }
        } else
            throw new GroupNotFoundException(String.valueOf(uuid));
    }

    @Transactional
    public void archiveStudents(UUID id, List<UUID> students) {
        if (groupRepository.existsById(id)) {
            for (UUID student : students) {
                StudentsByGroupEntity studentsByGroupEntity = studentsByGroupRepository.findById(new ComposeStudentsKey(id, student)).orElseThrow(() -> new StudentNotFoundInThisGroupException(id.toString(), student.toString()));
                try {
                    studentsByGroupEntity.setIsActive(false);
                    studentsByGroupRepository.save(studentsByGroupEntity);
                } catch (Exception e) {
                    throw new DatabaseUpdatingException(e.getMessage());
                }
            }
        } else
            throw new GroupNotFoundException(String.valueOf(id));
    }

    @Transactional
    public void graduateById(UUID uuid) {
        GroupEntity groupEntity = groupRepository.findById(uuid).orElseThrow(() -> new GroupNotFoundException(String.valueOf(uuid)));
        try {
            groupEntity.setIsActive(false);
            archiveGroupRepository.save(new ArchiveGroupDocument(groupEntity));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
        try {
            groupRepository.delete(groupEntity);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    @Scheduled(fixedRate = 86400000)
    public void checkGraduation() {
        List<GroupEntity> groups = groupRepository.findActiveGroupsToGraduate(LocalDate.now().minusDays(30));
        for (GroupEntity group : groups) {
            graduateById(group.getGroupId());
        }
    }

    //UTILITY

    private Page<GroupEntity> getActiveGroups(Pageable pageable, String courseId, String search) {
        Specification<GroupEntity> specification = Specification.where(GroupSpecifications.hasIsActive(true));

        if (courseId != null) specification = specification.and(GroupSpecifications.hasCourseId(courseId));
        if (search != null && !search.isEmpty()) specification = specification.and(GroupSpecifications.searchByQuery(search));

        return groupRepository.findAll(specification, pageable);
    }

    private Page<ArchiveGroupDocument> getArchiveGroups(Pageable pageable, String courseId, String search) {
        if (courseId != null && !search.isEmpty()) {
            return archiveGroupRepository.findByCourseIdAndSearch(UUID.fromString(courseId), search, pageable);
        } else if (courseId != null) {
            return archiveGroupRepository.findByCourseId(UUID.fromString(courseId), pageable);
        } else if (!search.isEmpty()) {
            return archiveGroupRepository.findBySearch(search, pageable);
        } else {
            return archiveGroupRepository.findAll(pageable);
        }
    }

    private GroupDto constructDto(IGroup group) {
        return new GroupDto(
                group.getGroupId(),
                group.getGroupName(),
                group.getStartDate(),
                group.getFinishDate(),
                group.getIsActive(),
                group.getCourseId(),
                group.getSlackLink(),
                group.getWhatsAppLink(),
                group.getSkypeLink(),
                group.getDeactivateAfter()
        );
    }

    private GroupEntity constructEntity(AddGroupDto group) {
        return new GroupEntity(
                group.getGroupName(),
                group.getStartDate(),
                group.getFinishDate(),
                group.getIsActive(),
                group.getCourseId(),
                group.getSlackLink(),
                group.getWhatsAppLink(),
                group.getSkypeLink(),
                group.getDeactivateAfter()
        );
    }
}