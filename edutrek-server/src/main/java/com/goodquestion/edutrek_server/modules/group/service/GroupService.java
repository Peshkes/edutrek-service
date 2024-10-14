package com.goodquestion.edutrek_server.modules.group.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;
import com.goodquestion.edutrek_server.error.ShareException.CourseNotFoundException;
import com.goodquestion.edutrek_server.error.ShareException.GroupNotFoundException;
import com.goodquestion.edutrek_server.error.ShareException.StudentAlreadyInThisGroupException;
import com.goodquestion.edutrek_server.error.ShareException.StudentNotFoundInThisGroupException;
import com.goodquestion.edutrek_server.modules.course.persistence.CourseRepository;
import com.goodquestion.edutrek_server.modules.group.dto.AddGroupDto;
import com.goodquestion.edutrek_server.modules.group.dto.ChangeLecturersDto;
import com.goodquestion.edutrek_server.modules.group.dto.PaginationGroupResponseDto;
import com.goodquestion.edutrek_server.modules.group.key.ComposeStudentsKey;
import com.goodquestion.edutrek_server.modules.group.persistence.groups.*;
import com.goodquestion.edutrek_server.modules.group.persistence.lecturers_by_group.*;
import com.goodquestion.edutrek_server.modules.group.persistence.lessons_and_webinars_by_weekday.*;
import com.goodquestion.edutrek_server.modules.group.persistence.students_by_group.*;
import com.goodquestion.edutrek_server.utility_service.ThreeFunction;
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
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;
    private final GroupArchiveRepository archiveRepository;
    private final LecturersByGroupRepository lecturersByGroupRepository;
    private final LecturersByGroupArchiveRepository lecturersByGroupArchiveRepository;
    private final StudentsByGroupRepository studentsByGroupRepository;
    private final StudentsByGroupArchiveRepository studentsByGroupArchiveRepository;
    private final LessonsByWeekdayRepository lessonsByWeekdayRepository;
    private final WebinarsByWeekdayRepository webinarsByWeekdayRepository;
    private final CourseRepository courseRepository;

    public BaseGroup getById(UUID groupId) {
        return repository.getGroupByGroupId(groupId).or(() -> archiveRepository.getGroupByGroupId(groupId)).orElseThrow(() -> new GroupNotFoundException(groupId.toString()));
    }

    public PaginationGroupResponseDto getAllPaginated(int page, int size, String courseId, Boolean isActive, String search) {
        Pageable pageable = PageRequest.of(page, size);

        List<? extends BaseGroup> groups;
        long totalGroups;

        Specification<? extends BaseGroup> specification = Specification.where(GroupSpecifications.hasIsActive(true));
        if (courseId != null) specification = specification.and(GroupSpecifications.hasCourseId(courseId));
        if (search != null && !search.isEmpty())
            specification = specification.and(GroupSpecifications.searchByQuery(search));

        if (isActive != null) {
            Page<? extends BaseGroup> retrievedGroups = isActive
                    ? repository.findAll((Specification<GroupEntity>) specification, pageable)
                    : archiveRepository.findAll((Specification<GroupArchiveEntity>) specification, pageable);

            groups = retrievedGroups.getContent();
            totalGroups = retrievedGroups.getTotalElements();

            return new PaginationGroupResponseDto(groups, totalGroups, page, size);
        } else {
            Specification<GroupEntity> activeSpecification = (Specification<GroupEntity>) specification;
            Specification<GroupArchiveEntity> archiveSpecification = (Specification<GroupArchiveEntity>) specification;

            Page<GroupEntity> mainPage = repository.findAll(activeSpecification, pageable);
            List<BaseGroup> results = new ArrayList<>(mainPage.getContent());

            int remainingElements = pageable.getPageSize() - results.size();
            if (remainingElements > 0) {
                Pageable archivePageable = PageRequest.of(0, remainingElements);
                Page<GroupArchiveEntity> archivePage = archiveRepository.findAll(archiveSpecification, archivePageable);
                results.addAll(archivePage.getContent());
            }

            long totalElements = repository.count(activeSpecification) + archiveRepository.count(archiveSpecification);

            return new PaginationGroupResponseDto(results, totalElements, pageable.getPageNumber(), pageable.getPageSize());
        }
    }

    @Transactional
    public void addEntity(AddGroupDto groupData) {
        if (!courseRepository.existsById(groupData.getCourseId()))
            throw new CourseNotFoundException(String.valueOf(groupData.getCourseId()));
        try {
            UUID groupId = repository.save(constructEntity(groupData)).getGroupId();
            for (int weekdayId : groupData.getLessons()) {
                try {
                    lessonsByWeekdayRepository.save(new LessonsByWeekdayEntity(groupId, weekdayId));
                } catch (Exception e) {
                    throw new DatabaseAddingException(e.getMessage());
                }
            }
            for (int weekdayId : groupData.getWebinars()) {
                try {
                    webinarsByWeekdayRepository.save(new WebinarsByWeekdayEntity(groupId, weekdayId));
                } catch (Exception e) {
                    throw new DatabaseAddingException(e.getMessage());
                }
            }
            changeLecturersToGroup(groupId, groupData.getLecturers());
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Transactional
    public void deleteById(UUID groupId) {
        if (repository.checkGroupExistsById(groupId)) {
            deleteGroupData(groupId, repository, lecturersByGroupRepository, studentsByGroupRepository);
        } else if (archiveRepository.checkGroupExistsById(groupId)) {
            deleteGroupData(groupId, archiveRepository, lecturersByGroupArchiveRepository, studentsByGroupArchiveRepository);
        } else {
            throw new GroupNotFoundException(String.valueOf(groupId));
        }
    }

    private void deleteGroupData(UUID groupId, IGroupRepository<? extends BaseGroup> groupRepo, ILecturerByGroupRepository<? extends BaseLecturerByGroup> lecturerRepo,
                                 IStudentsByGroupRepository<? extends BaseStudentsByGroup> studentRepo) {
        try {
            lecturerRepo.deleteByGroupId(groupId);
            studentRepo.deleteByGroupId(groupId);
            lessonsByWeekdayRepository.deleteByGroupId(groupId);
            webinarsByWeekdayRepository.deleteByGroupId(groupId);
            groupRepo.deleteGroupByGroupId(groupId);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    public void changeLecturersToGroup(UUID uuid, List<ChangeLecturersDto> changeLecturers) {
        if (repository.checkGroupExistsById(uuid)) {
            updateLecturersForGroup(uuid, changeLecturers, lecturersByGroupRepository, LecturersByGroupEntity::new);
        } else if (archiveRepository.checkGroupExistsById(uuid)) {
            updateLecturersForGroup(uuid, changeLecturers, lecturersByGroupArchiveRepository, LecturersByGroupArchiveEntity::new);
        } else {
            throw new GroupNotFoundException(String.valueOf(uuid));
        }
    }

    private <T extends BaseLecturerByGroup> void updateLecturersForGroup(UUID groupId, List<ChangeLecturersDto> changeLecturers,
                                                                         ILecturerByGroupRepository<T> repository, ThreeFunction<UUID, UUID, Boolean, T> entityConstructor) {
        repository.deleteByGroupId(groupId);
        for (ChangeLecturersDto changeLecturer : changeLecturers) {
            try {
                T entity = entityConstructor.apply(groupId, changeLecturer.getLecturerId(), changeLecturer.getIsWebinarist());
                repository.save(entity);
            } catch (Exception e) {
                throw new DatabaseAddingException(e.getMessage());
            }
        }
    }

    @Transactional
    public void updateById(UUID groupId, AddGroupDto groupData) {
        GroupEntity groupEntity = repository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId.toString()));

        if (groupData.getGroupName() != null) groupEntity.setGroupName(groupData.getGroupName());
        if (groupData.getFinishDate() != null) groupEntity.setFinishDate(groupData.getFinishDate());
        if (groupData.getIsActive() != null) groupEntity.setIsActive(groupData.getIsActive());
        if (groupData.getCourseId() != null) groupEntity.setCourseId(groupData.getCourseId());
        if (groupData.getSlackLink() != null) groupEntity.setSlackLink(groupData.getSlackLink());
        if (groupData.getWhatsAppLink() != null) groupEntity.setWhatsAppLink(groupData.getWhatsAppLink());
        if (groupData.getSkypeLink() != null) groupEntity.setSkypeLink(groupData.getSkypeLink());
        if (groupData.getDeactivateAfter() != null) groupEntity.setDeactivateAfter(groupData.getDeactivateAfter());

        changeSmthByWeekdays(groupData.getLessons(), groupId, lessonsByWeekdayRepository, LessonsByWeekdayEntity::new);
        changeSmthByWeekdays(groupData.getWebinars(), groupId, webinarsByWeekdayRepository, WebinarsByWeekdayEntity::new);

        changeLecturersToGroup(groupId, groupData.getLecturers());
    }

    private <T extends BaseSmthByWeekday> void changeSmthByWeekdays(List<Integer> items, UUID groupId, ISmthByWeekday<T> repository, BiFunction<UUID, Integer, T> entityConstructor) {
        if (items != null) {
            repository.deleteByGroupId(groupId);
            for (Integer item : items) {
                try {
                    T entity = entityConstructor.apply(groupId, item);
                    repository.save(entity);
                } catch (Exception e) {
                    throw new DatabaseAddingException(e.getMessage());
                }
            }
        }
    }

    public void addStudentsToGroup(UUID uuid, List<UUID> students) {
        if (repository.existsById(uuid)) {
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
        if (repository.existsById(fromId)) {
            if (repository.existsById(toId)) {
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
    public void archiveStudents(UUID id, List<UUID> students) {
        if (repository.existsById(id)) {
            for (UUID student : students) {
               BaseStudentsByGroup studentsByGroup = studentsByGroupRepository.getByGroupIdAndStudentId(id, student).or(() -> studentsByGroupArchiveRepository.getByGroupIdAndStudentId(id, student)).orElseThrow(() -> new StudentNotFoundInThisGroupException(id.toString(), student.toString()));
                try {
                    studentsByGroup.setIsActive(false);
                } catch (Exception e) {
                    throw new DatabaseUpdatingException(e.getMessage());
                }
            }
        } else
            throw new GroupNotFoundException(String.valueOf(id));
    }

    @Transactional
    public void graduateById(UUID uuid) {
        GroupEntity groupEntity = repository.findById(uuid).orElseThrow(() -> new GroupNotFoundException(String.valueOf(uuid)));

        try {
            groupEntity.setIsActive(false);
            for (StudentsByGroupEntity student : studentsByGroupRepository.getByGroupId(uuid)) {
                student.setIsActive(false);
            }
            for (LecturersByGroupEntity lecturer : lecturersByGroupRepository.getByGroupId(uuid))
                lecturersByGroupArchiveRepository.save(new LecturersByGroupArchiveEntity(lecturer));
            archiveRepository.save(new GroupArchiveEntity(groupEntity));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }

        try {
            studentsByGroupRepository.deleteByGroupId(uuid);
            lecturersByGroupRepository.deleteByGroupId(uuid);
            lessonsByWeekdayRepository.deleteByGroupId(uuid);
            webinarsByWeekdayRepository.deleteByGroupId(uuid);
            repository.delete(groupEntity);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    @Scheduled(fixedRate = 86400000)
    public void checkGraduation() {
        List<GroupEntity> groups = repository.findActiveGroupsToGraduate(LocalDate.now().minusDays(30));
        for (GroupEntity group : groups) {
            graduateById(group.getGroupId());
        }
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