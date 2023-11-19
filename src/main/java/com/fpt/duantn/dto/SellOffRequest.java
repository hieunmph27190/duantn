package com.fpt.duantn.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SellOffRequest {
    private List<SellOffProductRequest> sanPhams;
    private UUID idKhachHang;
    private Integer thanhToan;
    private Integer trangThaiTT;
    private String note;
}
