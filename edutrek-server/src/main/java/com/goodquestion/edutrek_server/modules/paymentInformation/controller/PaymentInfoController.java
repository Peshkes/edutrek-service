package com.goodquestion.edutrek_server.modules.paymentInformation.controller;

import com.goodquestion.edutrek_server.modules.paymentInformation.dto.PaymentInfoDataDto;
import com.goodquestion.edutrek_server.modules.paymentInformation.persistence.current.PaymentInfoEntity;

import com.goodquestion.edutrek_server.modules.paymentInformation.service.PaymentInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentInfoController {

    private final PaymentInfoService paymentInfoService;

//    @GetMapping("")
//    @ResponseStatus(HttpStatus.OK)
//    public List<CourseEntity> getAllCourses() {
//        return courseService.getAll();
//    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentInfoEntity getPaymentInfoById(@PathVariable UUID id) {
        return paymentInfoService.getById(id);
    }

    @PostMapping("")
    public ResponseEntity<String> addNewCourse(@RequestBody @Valid PaymentInfoDataDto paymentInfoData) {
        paymentInfoService.addEntity(paymentInfoData);
        return new ResponseEntity<>("Payment info created", HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePaymentInfoById(@PathVariable UUID id) {
        paymentInfoService.deleteById(id);
        return new ResponseEntity<>("Payment info deleted", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePaymentInfoById(@PathVariable UUID id, @RequestBody @Valid PaymentInfoDataDto paymentInfoData) {
        paymentInfoService.updateById(id, paymentInfoData);
        return new ResponseEntity<>("Payment info updated", HttpStatus.OK);
    }


}
