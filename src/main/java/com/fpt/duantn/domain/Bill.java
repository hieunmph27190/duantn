package com.fpt.duantn.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

@Builder
@Entity
@Table (name = "bill")
public class Bill {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreationTimestamp
    @Column(name = "create_date")
    private Timestamp billCreateDate;

    @Column(name = "payment_time")
    private Timestamp paymentTime;

    @Column(name = "payment_type")
    private Integer paymentType;

    @Column(name = "transactionno")
    private String transactionNo;

    @Column(name = "type")
    private Integer type;

    @Column(name = "shipe_fee")
    private BigDecimal shipeFee = new BigDecimal(0);

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount = new BigDecimal(0);


    @Pattern(regexp = "^(0|\\+\\d{2})\\d{9}$")
    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="address")
    private String address;

    @Column(name="note")
    private String note;

    //bi-directional many-to-one association to Customer
    @ManyToOne
    @JoinColumn(name = "customerid")
    private Customer customer;

    //bi-directional many-to-one association to Employee
    @ManyToOne
    @JoinColumn(name = "employeeid")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "payment_employee")
    private Employee paymentEmployee;

}
