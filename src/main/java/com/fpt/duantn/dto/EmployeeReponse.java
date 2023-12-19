package com.fpt.duantn.dto;

import com.fpt.duantn.domain.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
public class EmployeeReponse {

    private UUID id;

    private String name;

    private Boolean gender;

    private Timestamp dateOfBirth;

    private String address;

    private String email;

    private String phoneNumber;

    private Boolean image;

    private Integer type;

    private String userName;

    public EmployeeReponse(UUID id, String name, Boolean gender, Timestamp dateOfBirth, String address, String email, String phoneNumber,Boolean image, Integer type) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.image=image;
        this.type = type;
    }
}
