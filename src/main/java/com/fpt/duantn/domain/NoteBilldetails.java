package com.fpt.duantn.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Blob;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

@Entity
@Table (name = "customers")
public class NoteBilldetails {


    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="product_code")
    private String code;

    @Lob
    @Column(name="product_image")
    private Blob image;

    @Column(name="type")
    private Integer type;

    @ManyToOne
    @JoinColumn(name = "billdetail_id")
    private  BillDetail billDetailid;



}
