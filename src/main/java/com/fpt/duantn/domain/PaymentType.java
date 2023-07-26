package com.fpt.duantn.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

@Entity
@Table (name = "paymenttype")
public class PaymentType {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(name = "code")
    private String code;

    @Column(name = "payment_typename")
    private String name;


    @Column(name = "type")
    private Integer type;

//    @OneToMany(mappedBy = "bill")
//    private List<Bill> bill;

}
