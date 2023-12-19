package com.fpt.duantn.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
public class BillReponse {

    private UUID id;

    private UUID employeeId;

    private String employeeName;

    private UUID customerId;

    private String customerName;

    private Integer paymentType;

    private Timestamp billCreateDate;

    private BigDecimal shipeFee;

    private BigDecimal paymentAmount;

    private String phoneNumber;

    private String address;

    private Integer type;

    public BillReponse(UUID id, UUID employeeId, String employeeName, UUID customerId, String customerName, Integer paymentType, Timestamp billCreateDate, BigDecimal shipeFee,BigDecimal paymentAmount, String phoneNumber, String address, Integer type) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.customerId = customerId;
        this.customerName = customerName;
        this.paymentType = paymentType;
        this.paymentAmount = paymentAmount;
        this.billCreateDate = billCreateDate;
        this.shipeFee = shipeFee;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.type = type;
    }
}
