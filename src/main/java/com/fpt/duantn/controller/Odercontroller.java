package com.fpt.duantn.controller;


import com.fpt.duantn.domain.*;
import com.fpt.duantn.repository.CartRepository;
import com.fpt.duantn.repository.CartdetailRepository;
import com.fpt.duantn.repository.ProductDetailRepository;
import com.fpt.duantn.service.BillDetailService;
import com.fpt.duantn.service.BillService;
import com.fpt.duantn.service.CustomerService;
import com.fpt.duantn.util.SendMailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("api/orders")
public class Odercontroller {

    @Autowired
    BillDetailService billDetailService;

    @Autowired
    BillService billService;

    @Autowired
    CustomerService customerService;

    @Autowired
    SendMailUtil senMail;

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartdetailRepository cartdetailRepository;

    @GetMapping
    public ResponseEntity<List<Bill>>findAll() {
        return ResponseEntity.ok(billService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Bill>getById(@PathVariable("id") UUID id) {
        if(!billService.existsById(id)) {
          return   ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(billService.findById(id).get());
    }


    @GetMapping("/user/{email}")
    public ResponseEntity<List<?>>getByUser(@PathVariable("email") String email) {
        if(!customerService.existsByEmail(email)) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(billService.findByCustomer(customerService.findByEmail(email).get()));
    }


//
//@PostMapping("/{email}")
// public ResponseEntity<Bill> checkout(@PathVariable("email") String email, @RequestBody Cart cart){
//  if(!customerService.existsByEmail(email)) {
//    return ResponseEntity.notFound().build();
//  }
//  if(!cartRepository.existsById(cart.getCart_id())) {
//      return ResponseEntity.notFound().build();
//  }
// List<CartDetail> items = cartdetailRepository.findByCart(cart);
// Double amount=0.0;
//  for (CartDetail i: items) {
//      amount += i.getPrice();
//  }
//    Bill order = billService.save(
//            Bill.builder().billCreateDate(new Timestamp(System.currentTimeMillis())).
//            new Bill(0L,new Date(),amount,cart.getAddress(),cart.getPhone(),0,customerService.findByEmail(email).get()));
//
//  for (CartDetail i : items) {
//    OrderDetail orderdetail= new OrderDetail(0L, i.getQuantity(),i.getPrice(),i.getProductdetail(),order);
//     orderDetailRepository.save(orderdetail);
//   }
//  for (CartDetail i: items) {
//      cartdetailRepository.delete(i);
//  }
//    senMail.sendMailOrder(order);
//    return  ResponseEntity.ok(order);
//}
//
//
//
//     @GetMapping("cancel/{billid}")
//   public ResponseEntity<Void> cannel(@PathVariable("billid") Long id) {
//        if(!orderRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        Order order = orderRepository.findById(id).get();
//         order.setType(3);
//         orderRepository.save(order);
//      senMail.sendMailOrderCancel(order);
//       return ResponseEntity.ok().build();
//     }
//
//
//    @GetMapping("deliver/{billid}")
//    public ResponseEntity<Void> deliver(@PathVariable("billid") Long id) {
//        if(!orderRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        Order order = orderRepository.findById(id).get();
//        order.setType(1);
//        orderRepository.save(order);
//        senMail.sendMailOrderDeliver(order);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("success/{billid}")
//    public ResponseEntity<Void> success(@PathVariable("billid") Long id) {
//        if(!orderRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        Order order = orderRepository.findById(id).get();
//        order.setType(2);
//        orderRepository.save(order);
//        senMail.sendMailOrderSuccess(order);
//        updateProduct(order);
//        return ResponseEntity.ok().build();
//    }
//
//
//    public void updateProduct(Order order) {
//      List<OrderDetail> listOrderdetail =orderDetailRepository.findByOrder(order);
//    for (OrderDetail orderdetail : listOrderdetail) {
//    ProductDetail productDetail = productDetailRepository.findById(orderdetail.getProductdetail().getId()).get();
//     if(productDetail !=null) {
//     productDetail.setAmount(productDetail.getAmount() - orderdetail.getQuantity());
//     productDetail.setSold(productDetail.getSold() +orderdetail.getQuantity());
//     productDetailRepository.save(productDetail);
//     }
//}
//    }
}
