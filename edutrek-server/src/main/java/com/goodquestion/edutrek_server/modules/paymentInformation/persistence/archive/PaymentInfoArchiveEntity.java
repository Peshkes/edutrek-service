package com.goodquestion.edutrek_server.modules.paymentInformation.persistence.archive;

import com.goodquestion.edutrek_server.modules.paymentInformation.persistence.AbstractPaymentInformation;
import com.goodquestion.edutrek_server.modules.paymentInformation.persistence.current.PaymentInfoEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(schema = "archive", name = "payment_information")
public class PaymentInfoArchiveEntity extends AbstractPaymentInformation {

    public PaymentInfoArchiveEntity(PaymentInfoEntity paymentInfoEntity) {
        super(paymentInfoEntity);
    }
}
