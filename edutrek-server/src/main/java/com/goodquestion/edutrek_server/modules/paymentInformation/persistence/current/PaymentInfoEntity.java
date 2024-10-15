package com.goodquestion.edutrek_server.modules.paymentInformation.persistence.current;

import com.goodquestion.edutrek_server.modules.paymentInformation.persistence.AbstractPaymentInformation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(schema = "current", name = "payment_information")
public class PaymentInfoEntity extends AbstractPaymentInformation {

    public PaymentInfoEntity( UUID studentNum, LocalDate paymentDate, int paymentTypeId, int paymentUmount, String paymentDetails) {
        super( studentNum, paymentDate, paymentTypeId, paymentUmount, paymentDetails);
    }

}
