package com.fpt.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SellOnRequest {
    private List<SellOffProductRequest> sanPhams;
    private String phoneNumber;
    private String address;
    private String city;
    private String district;
    private String ward;
    private String note;
    private Integer paymentType;
}
