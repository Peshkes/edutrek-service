package com.goodquestion.edutrek_server.modules.paymentInformation.persistence.current;

import com.goodquestion.edutrek_server.modules.paymentInformation.persistence.IPaymentInfoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentInfoRepository extends IPaymentInfoRepository<PaymentInfoEntity>,JpaRepository<PaymentInfoEntity, UUID>, JpaSpecificationExecutor<PaymentInfoEntity> {

}
