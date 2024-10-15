package com.goodquestion.edutrek_server.modules.paymentTypes.service;

import com.goodquestion.edutrek_server.modules.paymentTypes.persistence.PaymentTypeEntity;
import com.goodquestion.edutrek_server.modules.paymentTypes.persistence.PaymentTypeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.goodquestion.edutrek_server.error.ShareException.StatusNotFoundException;


@Service
@RequiredArgsConstructor
public class PaymentTypeService {

    private final PaymentTypeRepository repository;

    public List<PaymentTypeEntity> getAll() {
        return repository.findAll();
    }

    public PaymentTypeEntity getById(int paymentTypeId) {
        return repository.findById(paymentTypeId).orElseThrow(() -> new StatusNotFoundException(paymentTypeId));
    }
}
