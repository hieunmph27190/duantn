package com.fpt.duantn.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;


import java.sql.Blob;
import java.util.Date;
import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Builder
@Entity
@Table (name = "employees")
public class Employee {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "type")
    private Integer type;


    @Column(name = "user_name")
    private String userName;

    @NotBlank
    @Column(name = "address")
    private String address;




    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dateof_birth")
    private Date dateOfBirth;

    @NotBlank
    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private Boolean gender;


    @JsonIgnore
    @Lob
    @Column(name = "image",updatable = false)
    private Blob image;



    @Column(name = "phone_number")
    private String phoneNumber;

    @NotBlank
    @Column(name = "name")
    private String name;

    @CreationTimestamp
    @Column(name = "create_date",updatable = false)
    private Timestamp createDate;

    @ManyToOne
    @JoinColumn(name = "roleid")
    private Role role;
}
