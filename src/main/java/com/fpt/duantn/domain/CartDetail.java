package com.fpt.duantn.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "carts_details")
public class CartDetail implements Serializable {

    @Id
    @Column(name = "cart_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartDetailId;

    @ManyToOne
    @JoinColumn(name = "productdetail_id")
    private ProductDetail productDetail;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name="type")
    private Integer type;

}
