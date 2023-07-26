package com.fpt.duantn.domain;

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

@Entity
@Table (name = "addresss")
public class Addresss {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="code")
    private String code;

    @Column(name="address")
    private String address;

    @Column(name="county")
    private String county;

    @Column(name="district")
    private String district;

    @Column(name="city")
    private String city;

    @Column(name="create_date")
    private Timestamp createDate;

    @Column(name="datemodifi_cation")
    private Timestamp datemodifiCation;

    @Column(name="type")
    private Integer type;

    @ManyToOne
    @JoinColumn(name = "customersid")
    private Customer customer;


}
