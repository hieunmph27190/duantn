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
    private long cart_detail_id;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "productdetail_id")
    private ProductDetail productdetail;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;



}
