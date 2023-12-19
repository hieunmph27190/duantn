package com.fpt.duantn.dto;


import java.math.BigDecimal;
import java.util.UUID;

public interface ProductBanHangResponse {
    UUID getId();
    String getCode();
    String getProductName();
    BigDecimal getMinPrice();
    BigDecimal getMaxPrice();
    UUID getImageId();
}
