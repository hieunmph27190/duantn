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
@Table (name = "sole")
public class Sole {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name="code")
    private String code;

    @Column(name="name")
    private String name;

    @Column(name="type")
    private Integer type;

    @CreationTimestamp
    @Column(name = "create_date",updatable = false)
    private Timestamp createDate;

//    @OneToMany(mappedBy="sole")
//    private List<Product> products;

}
