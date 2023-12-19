package com.fpt.duantn.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Blob;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

@Entity
@Table (name = "images")
public class Image {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonInclude
    @Lob
    @Column(name = "image")
    private Blob image;

    @Column(name = "type")
    private Integer type;

    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;


    public Image(UUID id, Integer type) {
        this.id = id;
        this.type = type;
    }
}
