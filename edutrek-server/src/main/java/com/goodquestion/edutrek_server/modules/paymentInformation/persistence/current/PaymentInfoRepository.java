package com.goodquestion.edutrek_server.modules.paymentInformation.persistence.current;

import com.goodquestion.edutrek_server.modules.paymentInformation.persistence.AbstractPaymentInformation;
import com.goodquestion.edutrek_server.modules.paymentInformation.persistence.IPaymentInfoRepository;
import com.goodquestion.edutrek_server.modules.students.persistence.AbstractStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.BitSet;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentInfoRepository extends IPaymentInfoRepository<PaymentInfoEntity>,JpaRepository<PaymentInfoEntity, UUID> {

}
