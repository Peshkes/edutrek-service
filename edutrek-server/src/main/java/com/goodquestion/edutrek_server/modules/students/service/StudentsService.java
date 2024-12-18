package com.goodquestion.edutrek_server.modules.students.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.ShareException;
import com.goodquestion.edutrek_server.error.ShareException.StudentNotFoundException;
import com.goodquestion.edutrek_server.modules.branch.persistence.BranchRepository;
import com.goodquestion.edutrek_server.modules.contacts.persistence.AbstractContacts;
import com.goodquestion.edutrek_server.modules.contacts.persistence.current.ContactsRepository;
import com.goodquestion.edutrek_server.modules.contacts.service.ContactsService;
import com.goodquestion.edutrek_server.modules.course.persistence.CourseRepository;
import com.goodquestion.edutrek_server.modules.log.service.LogService;
import com.goodquestion.edutrek_server.modules.paymentInformation.persistence.AbstractPaymentInformation;
import com.goodquestion.edutrek_server.modules.paymentInformation.service.PaymentInfoService;
import com.goodquestion.edutrek_server.modules.statuses.persistence.StatusEntity;
import com.goodquestion.edutrek_server.modules.statuses.persistence.StatusRepository;
import com.goodquestion.edutrek_server.modules.students.dto.FoundEntitiesDto;
import com.goodquestion.edutrek_server.modules.students.dto.StudentSearchDto;
import com.goodquestion.edutrek_server.modules.students.dto.StudentsDataDto;
import com.goodquestion.edutrek_server.modules.students.dto.StudentsFromContactDataDto;
import com.goodquestion.edutrek_server.modules.students.persistence.AbstractStudent;
import com.goodquestion.edutrek_server.modules.students.persistence.archive.StudentsArchiveEntity;
import com.goodquestion.edutrek_server.modules.students.persistence.archive.StudentsArchiveRepository;
import com.goodquestion.edutrek_server.modules.students.persistence.current.StudentEntity;
import com.goodquestion.edutrek_server.modules.students.persistence.current.StudentsRepository;
import com.goodquestion.edutrek_server.utility_service.logging.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.goodquestion.edutrek_server.utility_service.CommonUtilityMethods.checkStatusCourseBranch;
import static com.goodquestion.edutrek_server.utility_service.SearchUtilityMethods.findStudents;

@Service
@RequiredArgsConstructor

public class StudentsService {

    private final StudentsRepository repository;
    private final ContactsRepository contactsRepository;
    private final ContactsService contactService;
    private final StudentsArchiveRepository archiveRepository;
    private final StatusRepository statusRepository;
    private final PaymentInfoService paymentInfoService;
    private final BranchRepository branchRepository;
    private final CourseRepository courseRepository;
    private final LogService logService;

    @Loggable
    public StudentSearchDto getAll(int page, int pageSize, String search, Integer statusId, UUID groupId, UUID courseId) {
        StatusEntity status = statusId == null ? null : statusRepository.findById(statusId).orElse(null);
        Pageable pageable = PageRequest.of(page, pageSize);
        List<AbstractStudent> foundStudents;
        if (status != null && status.getStatusName().equalsIgnoreCase("Archive")) {
            FoundEntitiesDto foundEntitiesDto = findStudents(pageable, search, statusId, groupId, courseId, archiveRepository);
            foundStudents = foundEntitiesDto.getFoundStudents();
        } else {
            FoundEntitiesDto foundStudentEntities = findStudents(pageable, search, statusId, groupId, courseId, repository);
            foundStudents = foundStudentEntities.getFoundStudents();
            if (foundStudents.size() < pageSize) {
                FoundEntitiesDto foundStudentArchiveEntities = findStudents(PageRequest.of(page, pageSize - foundStudents.size()), search, statusId, groupId, courseId, archiveRepository);
                List<AbstractStudent> foundStudentsArchive = foundStudentArchiveEntities.getFoundStudents();
                if (!foundStudentsArchive.isEmpty())
                    foundStudents.addAll(foundStudentsArchive);
            }
        }
        return new StudentSearchDto(foundStudents, page, pageSize, foundStudents.size());
    }


    public AbstractStudent getById(UUID id) {
        return repository.getByStudentId(id).or(() -> archiveRepository.findById(id)).orElseThrow(() -> new StudentNotFoundException(id.toString()));
    }

    @Loggable
    @Transactional
    public void addEntity(StudentsDataDto studentData) {
        checkStatusCourseBranch(studentData.getBranchId(), studentData.getTargetCourseId(), studentData.getStatusId(), branchRepository, courseRepository,statusRepository);
        if (!repository.existsByPhoneOrEmail(studentData.getPhone(), studentData.getEmail())) {
            int statusId = statusRepository.findStatusEntityByStatusName("Student").getStatusId();
            AbstractContacts contact = contactsRepository.findByPhoneOrEmail(studentData.getPhone(), studentData.getEmail());
            if (contact == null) {
                repository.save(new StudentEntity(
                        studentData, statusId));
            } else {
                contactService.promoteContactToStudentById(contact.getContactId(), new StudentsFromContactDataDto(studentData.getFullPayment(), studentData.isDocumentsDone()));
            }
        }
    }



    @Loggable
    @Transactional
    public void deleteById(UUID id) {
        if (!repository.existsById(id)) throw new StudentNotFoundException(id.toString());
        logService.deleteById(id);
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Loggable
    @Transactional
    public void updateById(UUID id, StudentsDataDto studentData) {
        checkStatusCourseBranch(studentData.getBranchId(), studentData.getTargetCourseId(), studentData.getStatusId(), branchRepository, courseRepository,statusRepository);
        AbstractStudent entity = repository.getByStudentId(id).or(() -> archiveRepository.findById(id)).orElseThrow(() -> new StudentNotFoundException(id.toString()));
        updateEntity(studentData, entity);
    }

    private <T extends AbstractStudent> void updateEntity(StudentsDataDto studentData, T entity) {
        int statusId = statusRepository.findStatusEntityByStatusName("Student").getStatusId();
        entity.setContactName(studentData.getContactName());
        entity.setPhone(studentData.getPhone());
        entity.setEmail(studentData.getEmail());
        entity.setComment(studentData.getComment());
        entity.setStatusId(statusId);
        entity.setBranchId(studentData.getBranchId());
        entity.setTargetCourseId(studentData.getTargetCourseId());
        entity.setFullPayment(studentData.getFullPayment());
        entity.setDocumentsDone(studentData.isDocumentsDone());
    }

    @Loggable
    @Transactional
    public void moveToArchiveById(UUID id, String reason) {
        StudentEntity student = repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id.toString()));
        int statusId = statusRepository.findStatusEntityByStatusName("Archive").getStatusId();
        student.setStatusId(statusId);
        StudentsArchiveEntity studentArchEntity = new StudentsArchiveEntity(student, reason);
        try {
            List<AbstractPaymentInformation> payments = paymentInfoService.getByStudentId(0, 10, id).paymentsInfo();
            archiveRepository.save(studentArchEntity);
            payments.forEach(p -> paymentInfoService.movePaymentsToArchive(p.getPaymentId()));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }

    }

    @Loggable
    @Transactional
    public void graduateById(UUID id) {
        moveToArchiveById(id, "Finished course and graduated");
    }
}
