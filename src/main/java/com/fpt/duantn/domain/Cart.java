package com.fpt.duantn.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString

@Entity
@Table(name = "carts")
public class Cart implements Serializable {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cart_id;

    @Column(name = "amount")
    private int amount;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private int phone;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;




}
