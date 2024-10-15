package com.goodquestion.edutrek_server.modules.paymentInformation.persistence.current;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfoEntity, UUID> {}
