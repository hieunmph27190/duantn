package com.fpt.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Data


public class DataTablesResponse {
    private Integer draw;
    private Integer recordsTotal;
    private Long recordsFiltered;
    private Integer page;
    private Integer totalPage;
    private Integer size;
    private List<?> data;

    public DataTablesResponse(Integer draw, Integer recordsTotal, Long recordsFiltered, List<?> data) {
        this.draw = draw;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
    }
    public DataTablesResponse(Optional<Integer>draw, Page<?> page) {
        this.draw = draw.orElse(10);
        this.recordsTotal = page.getNumberOfElements();
        this.recordsFiltered = page.getTotalElements();
        this.data = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalPage = page.getTotalPages();
    }

}