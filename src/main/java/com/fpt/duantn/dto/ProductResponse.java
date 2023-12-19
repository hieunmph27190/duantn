package com.fpt.duantn.dto;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Category;
import com.fpt.duantn.domain.Product;
import com.fpt.duantn.domain.Sole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

//public class ProductResponse {
//
//    public UUID getId() ;
//
//    public String getCode() ;
//
//    public String getName() ;
//
//    public Integer getType() ;
//
//    public String getDescription() ;
//
//    public Timestamp getCreateDate() ;
//
//    public String getCategoryCode() ;
//    public String getCategoryName() ;
//
//    public String getSoleCode() ;
//    public String getSoleName() ;
//
//    public String getBrandCode() ;
//    public String getBrandName() ;
//
//    public UUID getImageId() ;
//
//    public Double getPriceMin() ;
//    public Double getPriceMax() ;
//}

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Category;
import com.fpt.duantn.domain.Product;
import com.fpt.duantn.domain.Sole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data


public class ProductResponse {

    private UUID id;

    private String code;

    private String name;

    private Integer type;

    private String description;

    private Timestamp createDate;

    private Category category;

    private Sole sole;

    private Brand brand;

    private UUID imageId;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.code = product.getCode();
        this.name = product.getName();
        this.type = product.getType();
        this.description = product.getDescription();
        this.createDate = product.getCreateDate();
        this.category = product.getCategory();
        this.sole = product.getSole();
        this.brand = product.getBrand();
    }
}