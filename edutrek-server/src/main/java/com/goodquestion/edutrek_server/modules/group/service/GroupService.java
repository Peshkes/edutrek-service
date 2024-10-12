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
import com.goodquestion.edutrek_server.modules.group.dto.PaginationGroupResponse;
import com.goodquestion.edutrek_server.modules.group.key.ComposeStudentsKey;
import com.goodquestion.edutrek_server.modules.group.persistence.groups.*;
import com.goodquestion.edutrek_server.modules.group.persistence.lecturers_by_group.*;
import com.goodquestion.edutrek_server.modules.group.persistence.lessons_by_weekday.*;
import com.goodquestion.edutrek_server.modules.group.persistence.students_by_group.*;
import com.goodquestion.edutrek_server.modules.group.persistence.webinars_by_weekday.IWebinarsByWeekdayRepository;
import com.goodquestion.edutrek_server.modules.group.persistence.webinars_by_weekday.WebinarsByWeekdayArchiveRepository;
import com.goodquestion.edutrek_server.modules.group.persistence.webinars_by_weekday.WebinarsByWeekdayArchiveEntity;
import com.goodquestion.edutrek_server.modules.group.persistence.webinars_by_weekday.WebinarsByWeekdayEntity;
import com.goodquestion.edutrek_server.modules.group.persistence.webinars_by_weekday.WebinarsByWeekdayRepository;
import com.goodquestion.edutrek_server.utility_service.ThreeFunction;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final LessonsByWeekdayArchiveRepository lessonsByWeekdayArchiveRepository;
    private final WebinarsByWeekdayRepository webinarsByWeekdayRepository;
    private final WebinarsByWeekdayArchiveRepository webinarsByWeekdayArchiveRepository;
    private final CourseRepository courseRepository;

    private final EntityManager entityManager;


    public List<GroupEntity> getAll() {
        return repository.findAll();
    }

    public GroupEntity getById(UUID groupId) {
        return repository.getGroupByGroupId(groupId).or(() -> archiveRepository.getGroupByGroupId(groupId)).orElseThrow(() -> new GroupNotFoundException(groupId.toString()));
    }

    public PaginationGroupResponse getAllPaginated(int page, int size, String courseId, Boolean isActive, String search) {
        Pageable pageable = PageRequest.of(page, size);

        List<? extends GroupEntity> groups;
        long totalGroups;
        Page<? extends GroupEntity> retrievedGroups;

        Specification<? extends GroupEntity> specification = Specification.where(GroupSpecifications.hasIsActive(true));
        if (courseId != null) specification = specification.and(GroupSpecifications.hasCourseId(courseId));
        if (search != null && !search.isEmpty())
            specification = specification.and(GroupSpecifications.searchByQuery(search));

        if (isActive != null) {
            retrievedGroups = isActive ? repository.findAll((Specification<GroupEntity>) specification, pageable) : archiveRepository.findAll((Specification<GroupArchiveEntity>) specification, pageable);
        } else
            retrievedGroups = repository.findAll((Specification<GroupEntity>) specification, pageable);

        groups = retrievedGroups.getContent();
        totalGroups = retrievedGroups.getTotalElements();

        return new PaginationGroupResponse(groups, totalGroups, page, size);
    }

    private  <T extends GroupEntity> PaginationGroupResponse getGroups(int page, int size, String courseId, Boolean isActive, String search, JpaSpecificationExecutor<T> repository) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<T> specification = Specification.where(GroupSpecifications.hasIsActive(true));
        if (courseId != null) specification = specification.and(GroupSpecifications.hasCourseId(courseId));
        if (search != null && !search.isEmpty())
            specification = specification.and(GroupSpecifications.searchByQuery(search));

        Page<T> retrievedGroups = repository.findAll(specification, pageable);

        List<T> groups = retrievedGroups.getContent();
        long totalGroups = retrievedGroups.getTotalElements();

        return new PaginationGroupResponse(groups, totalGroups, page, size);
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
            deleteGroupData(groupId, repository, lecturersByGroupRepository, studentsByGroupRepository, lessonsByWeekdayRepository, webinarsByWeekdayRepository);
        } else if (archiveRepository.checkGroupExistsById(groupId)) {
            deleteGroupData(groupId, archiveRepository, lecturersByGroupArchiveRepository, studentsByGroupArchiveRepository, lessonsByWeekdayArchiveRepository, webinarsByWeekdayArchiveRepository);
        } else {
            throw new GroupNotFoundException(String.valueOf(groupId));
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

    @Transactional
    public void updateById(UUID groupId, AddGroupDto groupData) {
        if (repository.checkGroupExistsById(groupId)) {
            updateGroupData(groupId, groupData, repository, lessonsByWeekdayRepository, LessonsByWeekdayEntity::new, webinarsByWeekdayRepository, WebinarsByWeekdayEntity::new);
        } else if (archiveRepository.checkGroupExistsById(groupId)) {
            updateGroupData(groupId, groupData, archiveRepository, lessonsByWeekdayArchiveRepository, LessonsByWeekdayArchiveEntity::new, webinarsByWeekdayArchiveRepository, WebinarsByWeekdayArchiveEntity::new);
        } else {
            throw new GroupNotFoundException(String.valueOf(groupId));
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
                StudentsByGroupEntity studentsByGroupEntity = studentsByGroupRepository.getByGroupIdAndStudentId(id, student).or(() -> studentsByGroupArchiveRepository.getByGroupIdAndStudentId(id, student)).orElseThrow(() -> new StudentNotFoundInThisGroupException(id.toString(), student.toString()));
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
        GroupEntity groupEntity = repository.getGroupByGroupId(uuid).orElseThrow(() -> new GroupNotFoundException(String.valueOf(uuid)));
        List<StudentsByGroupEntity> students = studentsByGroupRepository.getByGroupId(uuid);
        List<LecturersByGroupEntity> lecturers = lecturersByGroupRepository.getByGroupId(uuid);
        List<LessonsByWeekdayEntity> lessons = lessonsByWeekdayRepository.getByGroupId(uuid);
        List<WebinarsByWeekdayEntity> webinars = webinarsByWeekdayRepository.getByGroupId(uuid);

        try {
            studentsByGroupRepository.deleteByGroupId(uuid);
            lecturersByGroupRepository.deleteByGroupId(uuid);
            lessonsByWeekdayRepository.deleteByGroupId(uuid);
            webinarsByWeekdayRepository.deleteByGroupId(uuid);
            repository.delete(groupEntity);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }

        entityManager.detach(groupEntity);
        entityManager.detach(students);
        entityManager.detach(lecturers);
        entityManager.detach(lessons);
        entityManager.detach(webinars);

        try {
            groupEntity.setIsActive(false);
            for (StudentsByGroupEntity student : students) {student.setIsActive(false);}
            for (LecturersByGroupEntity lecturer : lecturers) lecturersByGroupArchiveRepository.save(new LecturersByGroupArchiveEntity(lecturer));
            for (LessonsByWeekdayEntity lesson : lessons) lessonsByWeekdayArchiveRepository.save(new LessonsByWeekdayArchiveEntity(lesson));
            for (WebinarsByWeekdayEntity webinar : webinars) webinarsByWeekdayArchiveRepository.save(new WebinarsByWeekdayArchiveEntity(webinar));
            archiveRepository.save(new GroupArchiveEntity(groupEntity));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }


    }

//    @Transactional
//    @Scheduled(fixedRate = 86400000)
//    public void checkGraduation() {
//        List<GroupEntity> groups = repository.findActiveGroupsToGraduate(LocalDate.now().minusDays(30));
//        for (GroupEntity group : groups) {
//            graduateById(group.getGroupId());
//        }
//    }

    //UTILITY

    @Transactional
    protected void deleteGroupData(UUID groupId, IGroupRepository<? extends GroupEntity> groupRepo, ILecturerByGroupRepository<? extends LecturersByGroupEntity> lecturerRepo,
                                   IStudentsByGroupRepository<? extends StudentsByGroupEntity> studentRepo, ILessonsByWeekdayRepository<? extends LessonsByWeekdayEntity> lessonRepo,
                                   IWebinarsByWeekdayRepository<? extends WebinarsByWeekdayEntity> webinarRepo) {
        try {
            lecturerRepo.deleteByGroupId(groupId);
            studentRepo.deleteByGroupId(groupId);
            lessonRepo.deleteByGroupId(groupId);
            webinarRepo.deleteByGroupId(groupId);
            groupRepo.deleteGroupByGroupId(groupId);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    private <T extends LecturersByGroupEntity> void updateLecturersForGroup(UUID groupId, List<ChangeLecturersDto> changeLecturers,
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
    protected <T extends GroupEntity, Lesson extends LessonsByWeekdayEntity, Webinar extends WebinarsByWeekdayEntity>
    void updateGroupData(UUID groupId, AddGroupDto groupData, IGroupRepository<T> groupRepository,
                         ILessonsByWeekdayRepository<Lesson> lessonsRepository, BiFunction<UUID, Integer, Lesson> lessonConstructor,
                         IWebinarsByWeekdayRepository<Webinar> webinarsRepository, BiFunction<UUID, Integer, Webinar> webinarConstructor) {

        T groupEntity = groupRepository.getGroupByGroupId(groupId).orElseThrow(() -> new GroupNotFoundException(groupId.toString()));

        if (groupData.getGroupName() != null) groupEntity.setGroupName(groupData.getGroupName());
        if (groupData.getFinishDate() != null) groupEntity.setFinishDate(groupData.getFinishDate());
        if (groupData.getIsActive() != null) groupEntity.setIsActive(groupData.getIsActive());
        if (groupData.getCourseId() != null) groupEntity.setCourseId(groupData.getCourseId());
        if (groupData.getSlackLink() != null) groupEntity.setSlackLink(groupData.getSlackLink());
        if (groupData.getWhatsAppLink() != null) groupEntity.setWhatsAppLink(groupData.getWhatsAppLink());
        if (groupData.getSkypeLink() != null) groupEntity.setSkypeLink(groupData.getSkypeLink());
        if (groupData.getDeactivateAfter() != null) groupEntity.setDeactivateAfter(groupData.getDeactivateAfter());

        List<Integer> lessons = groupData.getLessons();
        if (groupData.getLessons() != null) {
            lessonsRepository.deleteByGroupId(groupId);
            for (Integer lesson : lessons) {
                try {
                    Lesson entity = lessonConstructor.apply(groupId, lesson);
                    lessonsRepository.save(entity);
                } catch (Exception e) {
                    throw new DatabaseAddingException(e.getMessage());
                }
            }
        }

        List<Integer> webinars = groupData.getWebinars();
        if (groupData.getWebinars() != null) {
            webinarsRepository.deleteByGroupId(groupId);
            for (Integer webinar : webinars) {
                try {
                    Webinar entity = webinarConstructor.apply(groupId, webinar);
                    webinarsRepository.save(entity);
                } catch (Exception e) {
                    throw new DatabaseAddingException(e.getMessage());
                }
            }
        }

        changeLecturersToGroup(groupId, groupData.getLecturers());
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