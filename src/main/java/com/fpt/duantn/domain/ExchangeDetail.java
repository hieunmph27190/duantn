package com.fpt.duantn.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

//@Entity
//@Table (name = "exchangedetails")
public class ExchangeDetail {



    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="note")
    private String note;

    @Column(name="price")
    private BigDecimal price;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="type")
    private Integer type;

    //bi-directional many-to-one association to Exchange
    @ManyToOne
    @JoinColumn(name="exchangeid")
    private Exchange exchange;


    //bi-directional many-to-one association to ProductDetail
    @ManyToOne
    @JoinColumn(name="productdetailid")
    private ProductDetail productDetail;


}
