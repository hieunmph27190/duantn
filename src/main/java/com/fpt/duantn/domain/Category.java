package com.fpt.duantn.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;


import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

@Entity
@Table (name = "categories")
public class Category {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="code")
    private String code;

    @Column(name="category_name")
    private String name;

    @Column(name="type")
    private Integer type;

    @CreationTimestamp
    @Column(name = "create_date",updatable = false)
    private Timestamp createDate;

//    //bi-directional many-to-one association to ProductDetail
//    @OneToMany(mappedBy="categorys")
//    private List<ProductDetail> productDetails;


}
