package com.fpt.duantn.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.relational.core.sql.In;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

@Entity
@Table (name = "evaluate")
public class Eveluate {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "productDetaild")
    private ProductDetail productDetail;

    @ManyToOne
    @JoinColumn(name = "customersId")
    private Customer customer;

    @Column(name="comment")
    private String comment;

    @Column(name="type")
    private Integer type;

}
