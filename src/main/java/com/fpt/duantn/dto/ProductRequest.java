package com.fpt.duantn.dto;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Category;
import com.fpt.duantn.domain.ProductDetail;
import com.fpt.duantn.domain.Sole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRequest {

    private String code;
    private String name;
    private Integer type;
    private String description;

    private Category category;

    private Sole sole;

    private Brand brand;

    private List<ProductDetail> details;


}
