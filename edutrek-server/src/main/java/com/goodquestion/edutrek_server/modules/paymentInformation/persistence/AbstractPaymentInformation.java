package com.goodquestion.edutrek_server.modules.paymentInformation.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class AbstractPaymentInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    @Setter(AccessLevel.NONE)
    private UUID paymentId;

    @Column(name = "student_id")
    private UUID studentId;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_type_id")
    private int paymentTypeId;

    @Column(name = "payment_amount")
    private int paymentUmount;

    @Column(name = "payment_details")
    private String paymentDetails;

    public AbstractPaymentInformation(UUID studentNum, LocalDate paymentDate, int paymentTypeId, int paymentUmount, String paymentDetails) {
        this.paymentId = UUID.randomUUID();
        this.studentId = studentNum;
        this.paymentDate = paymentDate;
        this.paymentTypeId = paymentTypeId;
        this.paymentUmount = paymentUmount;
        this.paymentDetails = paymentDetails;
    }
}
