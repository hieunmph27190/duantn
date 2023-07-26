package com.fpt.duantn.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

@Entity
@Table (name = "promotion")
public class Promotion {

     @Id
     @Column(name = "id")
     @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "description")
    private String description;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "enddate")
    private Timestamp enddate;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "minimum_price")
    private BigDecimal minimumPrice;

    @Column(name = "promotion_name")
    private String promotionName;

    @Column(name = "type")
    private Integer type;


}
