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
@Table (name = "billdetails")
public class BillDetail {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "type")
    private Integer type;

    //bi-directional many-to-one association to Bill
    @ManyToOne
    @JoinColumn(name = "billid")
    private Bill bill;

    //bi-directional many-to-one association to ProductDetail
    @ManyToOne
    @JoinColumn(name = "product_detailid")
    private ProductDetail productDetail;
}
