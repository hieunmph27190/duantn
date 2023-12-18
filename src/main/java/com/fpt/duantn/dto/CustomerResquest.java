package com.fpt.duantn.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.duantn.domain.Customer;
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
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data


public class CustomerResquest {


    @NotNull
    private Integer type;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @NotBlank
    private String email;

    @NotNull
    private Boolean gender;

    @JsonIgnore
    @Lob
    private Blob image;

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^(0|\\+\\d{2})\\d{9}$")
    private String phoneNumber;

    private String city;

    private String district;

    private String ward;

    private String address;

    @CreationTimestamp
    @Column(name = "create_date",updatable = false)
    private Timestamp createDate;

    @Column(name = "password")
    private String password;

}
