package com.fpt.duantn.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data

@NoArgsConstructor
public class CustomerReponse {

    private UUID id;

    private String name;

    private Boolean gender;

    private Timestamp dateOfBirth;

    private String email;

    private String phoneNumber;

    private Boolean image;

    private Integer type;

    private String ward;

    private String city;

    private String district;

    private String address;



    public CustomerReponse(UUID id, String name, Boolean gender, Timestamp dateOfBirth, String address, String email, String phoneNumber, Boolean image, Integer type,String ward,String city,String district) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.image=image;
        this.type = type;
        this.ward = ward;
        this.city = city;
        this.district = district;
    }
}
