package com.fpt.duantn.payload.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^(0|\\+\\d{2})\\d{9}$")
    private String phoneNumber;

    @NotNull
    private Boolean gender;

    @NotBlank
    private String city;


    @NotBlank
    private String district;

    @NotBlank
    private String ward;

    @NotBlank
    private String address;


    
}
