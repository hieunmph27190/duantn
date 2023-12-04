package com.fpt.duantn.controller;



import com.fpt.duantn.service.BillDetailService;
import com.fpt.duantn.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/orderDetail")
public class OderDetailcontroller {

    @Autowired
    BillDetailService billDetailService;

    @Autowired
    BillService billService;

    @GetMapping("/order/{id}")
    public ResponseEntity<List<?>>getByOder(@PathVariable("id") UUID id) {
       if(!billDetailService.existsById(id)) {
           ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(billDetailService.findByBillId(id));
    }

}
