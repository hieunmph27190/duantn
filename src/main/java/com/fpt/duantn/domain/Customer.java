package com.fpt.duantn.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.sql.Blob;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

@Entity
@Table (name = "customers")
public class Customer {


    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="type")
    private Integer type;

    @Column(name = "dateof_birth")
    private Timestamp dateOfBirth;

    @Column(name="email")
    private String email;

    @Column(name="gender")
    private Boolean gender;

    @Lob
    @Column(name="image")
    private Blob image;

    @Column(name="last_name")
    private String lastName;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="first_name")
    private String firstName;

    @Column(name="buffer_name")
    private String bufferName;


    @OneToMany(mappedBy="customer")
    private List<Bill> bills;

}
