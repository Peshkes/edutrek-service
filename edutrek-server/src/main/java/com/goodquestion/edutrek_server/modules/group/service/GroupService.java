package com.goodquestion.edutrek_server.modules.group.service;

import com.goodquestion.edutrek_server.error.DatabaseException.*;
import com.goodquestion.edutrek_server.error.ShareException.*;
import com.goodquestion.edutrek_server.modules.course.persistence.CourseRepository;
import com.goodquestion.edutrek_server.modules.group.dto.*;
import com.goodquestion.edutrek_server.modules.group.key.ComposeStudentsKey;
import com.goodquestion.edutrek_server.modules.group.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final LecturersByGroupRepository lecturersByGroupRepository;
    private final StudentsByGroupRepository studentsByGroupRepository;
    private final LessonsByWeekdayRepository LessonsByWeekdayRepository;
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

        groupEntity.setGroupName(groupData.getGroupName());
        groupEntity.setFinishDate(groupData.getFinishDate());
        groupEntity.setIsActive(groupData.getIsActive());
        groupEntity.setCourse(courseRepository.findById(groupData.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException(groupData.getCourseId().toString())));
        groupEntity.setSlackLink(groupData.getSlackLink());
        groupEntity.setWhatsAppLink(groupData.getWhatsAppLink());
        groupEntity.setSkypeLink(groupData.getSkypeLink());
        groupEntity.setDeactivateAfter(groupData.getDeactivateAfter());

        try {
            groupRepository.save(groupEntity);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }

    }

    public PaginationResponse getAllPaginated(int page, int size, String courseId, Boolean isActive, String search) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<GroupEntity> specification = Specification.where(null);

        if (courseId != null) {
            specification = specification.and(GroupSpecifications.hasCourseId(courseId));
        }

        if (isActive != null) {
            specification = specification.and(GroupSpecifications.hasIsActive(isActive));
        }

        if (search != null && !search.isEmpty()) {
            specification = specification.and(GroupSpecifications.searchByQuery(search));
        }

        Page<GroupEntity> groups = groupRepository.findAll(specification, pageable);

        //TODO ARCHIVE

        return new PaginationResponse(groups.getContent().stream().map(this::constructDto).toList(), groups.getTotalElements(), page, size);
    }

    public void addStudentsToGroup(UUID uuid, List<UUID> students) {
        if (groupRepository.existsById(uuid)) {
            for (UUID student : students) {
                if (!studentsByGroupRepository.existsByGroupIdAndStudentId(uuid, student))
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
                    if (!studentsByGroupRepository.existsByGroupIdAndStudentId(toId, student)) {
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

    public void archiveStudents(String id, List<UUID> students) {
        //TODO ARCHIVE
    }

    public void graduateById(UUID uuid) {
        //TODO ARCHIVE
    }

    //UTILITY

    private GroupDto constructDto(GroupEntity groupEntity) {
        return new GroupDto(
                groupEntity.getGroupId(),
                groupEntity.getGroupName(),
                groupEntity.getStartDate(),
                groupEntity.getFinishDate(),
                groupEntity.getIsActive(),
                groupEntity.getCourse().getCourseId(),
                groupEntity.getSlackLink(),
                groupEntity.getWhatsAppLink(),
                groupEntity.getSkypeLink(),
                groupEntity.getDeactivateAfter()
        );
    }

    private GroupEntity constructEntity(GroupDto groupDto) {
        return new GroupEntity(
                groupDto.getGroupId(),
                groupDto.getGroupName(),
                groupDto.getStartDate(),
                groupDto.getFinishDate(),
                groupDto.getIsActive(),
                courseRepository.findById(groupDto.getCourseId())
                        .orElseThrow(() -> new CourseNotFoundException(groupDto.getCourseId().toString())),
                groupDto.getSlackLink(),
                groupDto.getWhatsAppLink(),
                groupDto.getSkypeLink(),
                groupDto.getDeactivateAfter()
        );
    }

    private GroupEntity constructEntity(AddGroupDto groupDto) {
        return new GroupEntity(
                groupDto.getGroupName(),
                groupDto.getStartDate(),
                groupDto.getFinishDate(),
                groupDto.getIsActive(),
                courseRepository.findById(groupDto.getCourseId())
                        .orElseThrow(() -> new CourseNotFoundException(groupDto.getCourseId().toString())),
                groupDto.getSlackLink(),
                groupDto.getWhatsAppLink(),
                groupDto.getSkypeLink(),
                groupDto.getDeactivateAfter()
        );
    }
}
