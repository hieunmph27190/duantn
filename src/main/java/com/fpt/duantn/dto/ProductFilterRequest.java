package com.fpt.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterRequest {
    private Double minPrice;
    private Double maxPrice;
    private List<UUID> brandIDs;
    private List<UUID> categoryIDs;
    private List<UUID> soleIDs;
    private List<UUID> colorIDs;
    private List<UUID> sizeIDs;
}
