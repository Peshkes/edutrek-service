package com.goodquestion.edutrek_server.modules.paymentInformation.dto;


import com.goodquestion.edutrek_server.modules.paymentInformation.persistence.AbstractPaymentInformation;


import java.util.List;

public record PaymentsInfoSearchDto(List<AbstractPaymentInformation> paymentsInfo, int page, int pageSize, int size) {}