package com.fpt.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductFilterRequest {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<UUID> brandIDs;
    private List<UUID> categoryIDs;
    private List<UUID> soleIDs;
    private List<UUID> colorIDs;
    private List<UUID> sizeIDs;
}
