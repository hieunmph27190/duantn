package com.fpt.duantn.controller;

import com.fpt.duantn.domain.BillDetail;
import com.fpt.duantn.domain.Rate;
import com.fpt.duantn.repository.ProductDetailRepository;
import com.fpt.duantn.repository.RateRepository;
import com.fpt.duantn.service.BillDetailService;
import com.fpt.duantn.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("api/rates")
public class RateApi {

    @Autowired
    RateRepository rateRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    BillDetailService billDetailService;

    @Autowired
    ProductDetailRepository productDetailRepository;


    @GetMapping()
    public ResponseEntity<List<Rate>> findAll() {
        return ResponseEntity.ok(rateRepository.findAllByOrderByIdDesc());
    }


    @GetMapping("{billdetailid}")
    public ResponseEntity<Rate> finByid(@PathVariable UUID billdetailid) {
        if(!billDetailService.existsById(billdetailid)) {
          return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rateRepository.findByBillDetail(billDetailService.findById(billdetailid).get()));
    }


    @GetMapping("/productdetail/{id}")
    public ResponseEntity<List<Rate>> findByProduct(@PathVariable("id") UUID id) {
        if (!productDetailRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rateRepository.findByProductdetailOrderByIdDesc(productDetailRepository.findById(id).get()));
    }


//    @PostMapping
//    public ResponseEntity<Rate> post(@RequestBody Rate rate) {
//
//        if (!userRepository.existsById(rate.getUser().getUserid())) {
//            return ResponseEntity.notFound().build();
//        }
//        System.out.println(rate.getUser().getUserid());
//
//        if (!productDetailRepository.existsById(rate.getProductdetail().getId())) {
//            return ResponseEntity.notFound().build();
//        }
//        if (!orderDetailRepository.existsById(rate.getOrderdetail().getBilldetailid())) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(rateRepository.save(rate));
//    }


    @PostMapping
    public ResponseEntity<Rate> post(@RequestBody Rate rate) {

        if (rate.getCustomer() == null || rate.getCustomer().getId() == null) {
            System.out.println("User or User ID is null");
            return ResponseEntity.notFound().build();
        }
        System.out.println("User ID: " + rate.getCustomer().getId());

        if (rate.getProductdetail() == null || rate.getProductdetail().getId() == null) {
            System.out.println("ProductDetail or ProductDetail ID is null");
            return ResponseEntity.notFound().build();
        }
        System.out.println("producdetail Id: " + rate.getProductdetail().getId());

        if (rate.getBillDetail() == null || rate.getBillDetail().getId() == null) {
            System.out.println("OrderDetail or OrderDetail ID is null");
            return ResponseEntity.notFound().build();
        }
        System.out.println("getOrderdetail Id: " + rate.getBillDetail().getId());


        return ResponseEntity.ok(rateRepository.save(rate));
    }





    @PutMapping
    public ResponseEntity<Rate> put(@RequestBody Rate rate) {
        if (!rateRepository.existsById(rate.getId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rateRepository.save(rate));
    }



    @DeleteMapping("{id}")
      public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
          if(!rateRepository.existsById(id)) {
              return  ResponseEntity.notFound().build();
          }
           rateRepository.deleteById(id);
          return ResponseEntity.ok().build();
      }



  }




