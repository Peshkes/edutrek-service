package com.goodquestion.edutrek_server.modules.paymentInformation.service;

import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;
import com.goodquestion.edutrek_server.error.ShareException.*;
import com.goodquestion.edutrek_server.modules.paymentInformation.dto.PaymentInfoDataDto;
import com.goodquestion.edutrek_server.modules.paymentInformation.persistence.current.PaymentInfoEntity;
import com.goodquestion.edutrek_server.modules.paymentInformation.persistence.current.PaymentInfoRepository;
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

//    @Cacheable(key = "#root.methodName")
//    public List<CourseEntity> getAll() {
//        return repository.findAll();
//    }

    public PaymentInfoEntity getById(UUID paymentInfoId) {
        return repository.findById(paymentInfoId).orElseThrow(() -> new PaymentInfoNotFoundException(String.valueOf(paymentInfoId)));
    }

    @Transactional
    @CacheEvict(key = "{'getAll'}")
    public void addEntity(PaymentInfoDataDto paymentInfoDataDtoData) {
        try {
            repository.save(new PaymentInfoEntity(
                    paymentInfoDataDtoData.getStudentId(),
                    paymentInfoDataDtoData.getPaymentDate(),
                    paymentInfoDataDtoData.getPaymentTypeId(),
                    paymentInfoDataDtoData.getPaymentUmount(),
                    paymentInfoDataDtoData.getPaymentDetails()));
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
