package com.fpt.duantn.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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



//    @OneToMany(mappedBy="color")
//    private List<ProductDetail> productDetails;


}
