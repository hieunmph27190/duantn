package com.fpt.duantn.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
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


public class BillUpdateResquest {
    @NotNull
    private UUID id;

    private Double paymentAmount;

    private String address;

    private String note;

    @NotNull
    private Integer type;

}
