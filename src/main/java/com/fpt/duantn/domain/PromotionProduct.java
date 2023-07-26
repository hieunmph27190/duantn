package com.fpt.duantn.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

@Entity
@Table (name = "promotionproduct")
public class PromotionProduct {



    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="type")
    private Integer type;

    //bi-directional many-to-one association to Exchange
    @ManyToOne
    @JoinColumn(name="promotionid")
    private Promotion promotion;

    //bi-directional many-to-one association to ProductDetail
    @ManyToOne
    @JoinColumn(name="productdetailid")
    private ProductDetail productDetail;






}
