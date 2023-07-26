package com.fpt.duantn.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

@Entity
@Table (name = "bill")
public class Bill {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "type")
    private Integer type;

    //bi-directional many-to-one association to Customer
    @ManyToOne
    @JoinColumn(name = "customerid")
    private Customer customer;

    //bi-directional many-to-one association to Employee
    @ManyToOne
    @JoinColumn(name = "paymenttypeid")
    private Employee employee;

    //bi-directional many-to-one association to BillDetail
    @OneToMany(mappedBy = "bill")
    private List<BillDetail> billDetails;

    //bi-directional many-to-one association to Exchange
    @OneToMany(mappedBy = "bill")
    private List<Exchange> exchanges;

}
