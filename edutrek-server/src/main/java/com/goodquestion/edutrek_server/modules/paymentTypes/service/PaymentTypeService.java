package com.goodquestion.edutrek_server.modules.paymentTypes.service;

import com.goodquestion.edutrek_server.modules.paymentTypes.persistence.PaymentTypeEntity;
import com.goodquestion.edutrek_server.modules.paymentTypes.persistence.PaymentTypeRepository;

import com.goodquestion.edutrek_server.utility_service.logging.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.goodquestion.edutrek_server.error.ShareException.StatusNotFoundException;


@Service
@RequiredArgsConstructor
public class PaymentTypeService {

    private final PaymentTypeRepository repository;

    @Loggable
    public List<PaymentTypeEntity> getAll() {
        return repository.findAll();
    }

    @Loggable
    public PaymentTypeEntity getById(int paymentTypeId) {
        return repository.findById(paymentTypeId).orElseThrow(() -> new StatusNotFoundException(paymentTypeId));
    }
}
