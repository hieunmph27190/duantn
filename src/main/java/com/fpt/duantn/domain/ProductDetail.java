package com.fpt.duantn.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

@Entity
@Table (name = "productdetail")
public class ProductDetail {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "type")
    private Integer type;


    //bi-directional many-to-one association to Color
    @ManyToOne
    @JoinColumn(name = "colorid")
    private Color color;

    @ManyToOne
//    @Cascade({CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "productid")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "sizeid")
    private Size size;



//    @OneToMany(mappedBy = "productDetail")
//    private List<Promotion_Product> promotionProducts;


}
