package com.fpt.duantn.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

@Entity
@Table (name = "colors")
public class Color {


    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Column(name="code")
    private String code;
    @NotBlank
    @Column(name="color_name")
    private String name;
    @NotNull
    @Column(name="type")
    private Integer type;

    @CreationTimestamp
    @Column(name = "create_date",updatable = false)
    private Timestamp createDate;

//    @OneToMany(mappedBy="color")
//    private List<ProductDetail> productDetails;


}
