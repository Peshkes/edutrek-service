package com.goodquestion.edutrek_server.modules.paymentInformation.persistence.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentInfoArchiveRepository extends JpaRepository<PaymentInfoArchiveEntity, UUID> {}
