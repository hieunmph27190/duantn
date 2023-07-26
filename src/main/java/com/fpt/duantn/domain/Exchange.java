package com.fpt.duantn.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

@Entity
@Table (name = "exchange")
public class Exchange {


    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="date_Time")
    private Timestamp date_Time;

    @Column(name="note")
    private String note;

    @Column(name="type")
    private Integer type;

    //bi-directional many-to-one association to Bill
    @ManyToOne
    @JoinColumn(name="billId")
    private Bill bill;

    //bi-directional many-to-one association to Employee
    @ManyToOne
    @JoinColumn(name="employeeid")
    private Employee employee;

//    //bi-directional many-to-one association to ExchangeDetail
//    @OneToMany(mappedBy="exchange")
//    private List<ExchangeDetail> exchangeDetails;

}
