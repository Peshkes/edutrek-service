package com.goodquestion.edutrek_server.modules.paymentInformation.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;
import com.goodquestion.edutrek_server.error.ShareException.PaymentInfoNotFoundException;
import com.goodquestion.edutrek_server.error.ShareException.StudentNotFoundException;
import com.goodquestion.edutrek_server.modules.paymentInformation.dto.PaymentInfoDataDto;
import com.goodquestion.edutrek_server.modules.paymentInformation.persistence.AbstractPaymentInformation;
import com.goodquestion.edutrek_server.modules.paymentInformation.persistence.archive.PaymentInfoArchiveRepository;
import com.goodquestion.edutrek_server.modules.paymentInformation.persistence.current.PaymentInfoEntity;
import com.goodquestion.edutrek_server.modules.paymentInformation.persistence.current.PaymentInfoRepository;
import com.goodquestion.edutrek_server.modules.students.persistence.current.StudentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames={"payment_information"})
public class PaymentInfoService {

    private final PaymentInfoRepository repository;
    private final PaymentInfoArchiveRepository archiveRepository;
    private final StudentsRepository studentsRepository;


    public AbstractPaymentInformation getByPaymentId(UUID paymentId) {
        return repository.findByPaymentId(paymentId).or(() -> archiveRepository.findById(paymentId)).orElseThrow(() -> new PaymentInfoNotFoundException(paymentId.toString()));
    }
    public AbstractPaymentInformation getByStudentId(UUID studentId) {
        return repository.findByStudentId(studentId).or(() -> archiveRepository.findById(studentId)).orElseThrow(() -> new PaymentInfoNotFoundException(studentId.toString()));
    }

    @Transactional
    @CacheEvict(key = "{'getAll'}")
    public void addEntity(PaymentInfoDataDto paymentInfoDtoData) {
        if (!studentsRepository.existsById(paymentInfoDtoData.getStudentId())) {
            throw new StudentNotFoundException(String.valueOf(paymentInfoDtoData.getStudentId()));
        }
        try {
            repository.save(new PaymentInfoEntity(
                    paymentInfoDtoData.getStudentId(),
                    paymentInfoDtoData.getPaymentTypeId(),
                    paymentInfoDtoData.getPaymentUmount(),
                    paymentInfoDtoData.getPaymentDetails()));
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Transactional
    @CachePut(key = "#id")
    public void deleteById(UUID id) {
        if (!repository.existsById(id))
            throw new PaymentInfoNotFoundException(String.valueOf(id));
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    @CachePut(key = "#id")
    public void updateById(UUID id, PaymentInfoDataDto paymentInfoDataDto) {
        PaymentInfoEntity paymentEntity = repository.findById(id).orElseThrow(() -> new PaymentInfoNotFoundException(String.valueOf(id)));
        paymentEntity.setStudentId(paymentInfoDataDto.getStudentId());
        paymentEntity.setPaymentDate(paymentInfoDataDto.getPaymentDate());
        paymentEntity.setPaymentTypeId(paymentInfoDataDto.getPaymentTypeId());
        paymentEntity.setPaymentUmount(paymentInfoDataDto.getPaymentUmount());
        paymentEntity.setPaymentDetails(paymentInfoDataDto.getPaymentDetails());

        try {
            repository.save(paymentEntity);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }

    }
}
