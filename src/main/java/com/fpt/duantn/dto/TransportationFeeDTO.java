package com.fpt.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransportationFeeDTO {

    public static final Integer heightProduct = 20;

    public static final Integer lengthProduct = 40;

    public static final Integer weightProduct = 500;

    public static final Integer widthProduct = 20;

    private  Integer toDistrictId;

    private String toWardCode;

    private Integer insuranceValue;

    private Integer quantity;

}
