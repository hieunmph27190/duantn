package com.fpt.duantn.dto;

import com.fpt.duantn.domain.Product;
import com.fpt.duantn.domain.ProductDetail;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
public class BillDetailReponse {
    private UUID id;

    private ProductDetail productDetail;

    private Product product;

    private BigDecimal price;

    private Integer quantity;

    private Integer type;

    public BillDetailReponse(UUID id,Product product, ProductDetail productDetail, BigDecimal price, Integer quantity, Integer type) {
        this.id = id;
        this.productDetail = productDetail;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
    }
}
