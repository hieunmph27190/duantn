package com.fpt.duantn.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.sql.Blob;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

@Entity
@Table (name = "employees")
public class Employee {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "password")
    private String password;

    @Column(name = "type")
    private Integer type;

    @Column(name = "user_name")
    private String user_name;

    //bi-directional many-to-one association to Person
    @Column(name = "address")
    private String address;

    @Column(name = "dateof_birth")
    private Timestamp dateOfBirth;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private Boolean gender;

    @Lob
    @Column(name = "image")
    private Blob image;


    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "buffer_name")
    private String bufferName;


//    bi-directional many-to-one association to Role
    @ManyToOne
    @JoinColumn(name = "roleid")
    private Role role;



//    //bi-directional many-to-one association to Bill
//    @OneToMany(mappedBy = "employee")
//    private List<Bill> bills;
//
//
//
//    //bi-directional many-to-one association to Exchange
//    @OneToMany(mappedBy = "employee")
//    private List<Exchange> exchanges;
}
