package com.fpt.duantn.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;


import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Date;
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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dateof_birth")
    private Date dateOfBirth;

    @Column(name="email")
    private String email;


    @Column(name="gender")
    private Boolean gender;

    @JsonIgnore
    @Lob
    @Column(name="image")
    private Blob image;

    @NotBlank
    @Column(name="name")
    private String name;

    @NotBlank
    @Pattern(regexp = "^(0|\\+\\d{2})\\d{9}$")
    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="ward")
    private String ward;

    @Column(name="city")
    private String city;

    @Column(name="district")
    private String district;

    @Column(name="address")
    private String address;

    @CreationTimestamp
    @Column(name = "create_date",updatable = false)
    private Timestamp createDate;


    @Column(name = "password")
    private String password;

}
