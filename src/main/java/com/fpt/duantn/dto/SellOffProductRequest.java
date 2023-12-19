package com.fpt.duantn.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SellOffProductRequest {
    @NonNull
    private UUID id;
    @NonNull
    private Integer quantity;
}
