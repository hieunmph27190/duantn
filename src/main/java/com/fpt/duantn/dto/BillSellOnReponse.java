package com.fpt.duantn.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
public class BillSellOnReponse {

    private UUID id;

    private Timestamp billCreateDate;

    private BigDecimal shipeFee;

    private String phoneNumber;

    private String address;

    private BigDecimal paymentAmount;
    private Integer paymentType;

    private Integer type;

    public BillSellOnReponse(UUID id, Timestamp billCreateDate, BigDecimal shipeFee, String phoneNumber, String address, BigDecimal paymentAmount, Integer type,Integer paymentType) {
        this.id = id;
        this.billCreateDate = billCreateDate;
        this.shipeFee = shipeFee;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.paymentAmount = paymentAmount;
        this.paymentType = paymentType;
        this.type = type;
    }
}
