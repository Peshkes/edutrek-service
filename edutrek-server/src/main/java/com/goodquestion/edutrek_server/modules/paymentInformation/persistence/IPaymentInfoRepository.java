package com.goodquestion.edutrek_server.modules.paymentInformation.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.BitSet;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface IPaymentInfoRepository<T extends  AbstractPaymentInformation> extends JpaRepository<T, UUID> {
    Optional<AbstractPaymentInformation> findByPaymentId(UUID paymentInfoId);

    Optional<AbstractPaymentInformation> findByStudentId(UUID studentId);
}