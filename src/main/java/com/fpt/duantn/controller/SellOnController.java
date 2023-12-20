package com.fpt.duantn.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.duantn.domain.*;
import com.fpt.duantn.dto.SellOffProductRequest;
import com.fpt.duantn.dto.SellOffRequest;
import com.fpt.duantn.dto.SellOnRequest;
import com.fpt.duantn.models.User;
import com.fpt.duantn.payload.response.MessageResponse;
import com.fpt.duantn.repository.CartdetailRepository;
import com.fpt.duantn.security.services.AuthenticationService;
import com.fpt.duantn.service.BillDetailService;
import com.fpt.duantn.service.BillService;
import com.fpt.duantn.service.CustomerService;
import com.fpt.duantn.service.ProductDetailService;
import com.fpt.duantn.service.impl.GiaoHangTietKiemServiceImpl;
import com.fpt.duantn.util.SendMailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/sellon")
public class SellOnController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BillService billService;
    @Autowired
    private BillDetailService billDetailService;
    @Autowired
    private ProductDetailService productDetailService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private GiaoHangTietKiemServiceImpl giaoHangTietKiemService;

    @Autowired
    private SendMailUtil sendMailUtil;
    @Autowired
    private CartdetailRepository cartdetailRepository;

    @PostMapping ("/calculate-money")
    public ResponseEntity<?> calculateMoney(@RequestBody SellOnRequest sellOnRequest){
        List<SellOffProductRequest> sellOffProductRequests= sellOnRequest.getSanPhams();
        Double sum =0D;
       if (sellOffProductRequests.size()>0){
           for (SellOffProductRequest request : sellOffProductRequests){
               ProductDetail productDetail =  productDetailService.findById(request.getId()).orElse(null);
               if (request.getId()==null||request.getQuantity()==null){
                   return ResponseEntity.badRequest().body("Thông tin sản phẩm hoặc số lượng bị thiếu");
               }
               if (productDetail == null){
                   return ResponseEntity.badRequest().body("Sản phẩm "+request.getId()+" không tồn tại hoặc đã ngừng kinh doanh");
               }
               sum+=request.getQuantity()*productDetail.getPrice().doubleValue();
           }

       }
        return ResponseEntity.ok(sum);

    }

    @GetMapping ("/calculate-money/{billID}")
    public ResponseEntity<?> calculateMoney(@PathVariable Optional<UUID> billID){
        if (!billService.existsById(billID.orElse(null))){
            return ResponseEntity.badRequest().body("Hóa đơn "+billID.orElse(null)+" không tồn tại");
        }
        Optional<Double> sumMoney = billDetailService.sumMoneyByBillIdAndType(billID.orElse(null),null);
        return ResponseEntity.ok(sumMoney.orElse(null));
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping ()
    public ResponseEntity<?> add(@RequestBody() SellOnRequest sellOnRequest, Authentication authentication) {

        if (sellOnRequest.getPaymentType()==null||sellOnRequest.getAddress()==null||sellOnRequest.getCity()==null||sellOnRequest.getDistrict()==null||sellOnRequest.getWard()==null||sellOnRequest.getNote()==null||sellOnRequest.getPhoneNumber()==null||sellOnRequest.getSanPhams()==null) {
            return ResponseEntity.badRequest().body("Thông tin Không đầy đủ");
        }
        List<SellOffProductRequest>sellOffProductRequests = sellOnRequest.getSanPhams();
        if (sellOffProductRequests.size()<=0){
            return ResponseEntity.badRequest().body("Đơn hàng trống !");
        }

        List<BillDetail> billDetails = new ArrayList<>();
        Double sum =0D;
        Integer sumQ =0;
        for (SellOffProductRequest request : sellOffProductRequests){
            if (request.getId()==null||request.getQuantity()==null){
                return ResponseEntity.badRequest().body("Thông tin sản phẩm hoặc số lượng bị thiếu");
            }
            ProductDetail productDetail =  productDetailService.findById(request.getId()).orElse(null);
            if (productDetail == null||productDetail.getType().equals(0)||productDetail.getProduct().getType().equals(0)){
                return ResponseEntity.badRequest().body("Sản phẩm "+request.getId()+" không tồn tại hoặc đã ngừng kinh doanh");
            }
            if (productDetail.getAmount()<request.getQuantity()){
                return ResponseEntity.badRequest().body("Sản phẩm "+request.getId()+" không đủ số lượng");
            }
            if (request.getQuantity()<=0){
                return ResponseEntity.badRequest().body("Sản phẩm "+request.getId()+" số lượng phải lớn hơn 0");
            }
            BillDetail billDetail = new BillDetail();
            billDetail.setProductDetail(productDetail);
            billDetail.setPrice(productDetail.getPrice());
            billDetail.setQuantity(request.getQuantity());
            billDetail.setType(1);
            billDetails.add(billDetail);
            if (sellOnRequest.getPaymentType().equals(2)){
                productDetail.setAmount(productDetail.getAmount()- billDetail.getQuantity());
            }
            sum+=request.getQuantity()*productDetail.getPrice().doubleValue();
            sumQ+=request.getQuantity();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Double shipFee = 0D;
        try {
            ResponseEntity responseEntity = giaoHangTietKiemService.tinhShip(sellOnRequest.getCity(),sellOnRequest.getDistrict(),sellOnRequest.getWard(),sumQ,((long)sum.doubleValue()));
            if (responseEntity.getStatusCode()== HttpStatus.OK){
                JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody().toString());
                shipFee = jsonNode.get("data").get("total").asDouble();
            }else {
                return responseEntity;
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Không thể tính tiên ship ! Kiểm tra lại địa chỉ ");
        }
        Bill newBill = new Bill();
        User user = authenticationService.loadUserByUsername(authentication.getName());
        Customer customer =  customerService.findById(user.getId()).orElse(null);
        newBill.setTransactionNo((newBill.getTransactionNo()==null?"": newBill.getTransactionNo()+"\n\n")+customer.getId()+" :Khách hàng: "+customer.getName() + " : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"))+" Đã Tạo Bill : ");
        newBill.setTransactionNo((newBill.getTransactionNo()==null?"": newBill.getTransactionNo())
                +"\nSet ShipeFee : "+newBill.getShipeFee()+" -> "+shipFee);
        newBill.setShipeFee(new BigDecimal(shipFee));

        newBill.setCustomer(customer);

        newBill.setTransactionNo((newBill.getTransactionNo()==null?"": newBill.getTransactionNo())
                + "\nSet Address : "+newBill.getAddress()+" -> "+sellOnRequest.getAddress());
        newBill.setAddress(sellOnRequest.getAddress());

        newBill.setTransactionNo((newBill.getTransactionNo()==null?"": newBill.getTransactionNo())
                + "\nSet Phone Number : "+newBill.getPhoneNumber()+" -> "+sellOnRequest.getPhoneNumber());
        newBill.setPhoneNumber(sellOnRequest.getPhoneNumber());

        newBill.setTransactionNo((newBill.getTransactionNo()==null?"": newBill.getTransactionNo())
                + "\nSet Note : \n"+newBill.getNote()+"\n ----> \n"+sellOnRequest.getNote());
        newBill.setNote(sellOnRequest.getNote());

       if (sellOnRequest.getPaymentType().equals(2)||sellOnRequest.getPaymentType().equals(-2)){
           newBill.setTransactionNo((newBill.getTransactionNo()==null?"": newBill.getTransactionNo())
                   + "\nSet PaymentType : "+newBill.getPaymentType()+" -> "+sellOnRequest.getPaymentType());
           newBill.setPaymentType(sellOnRequest.getPaymentType());
            if (sellOnRequest.getPaymentType().equals(2)){
                newBill.setTransactionNo((newBill.getTransactionNo()==null?"": newBill.getTransactionNo())
                        +"\nSet Type : "+newBill.getType()+" -> "+"-2");
                newBill.setType(-2);
                newBill.setTransactionNo((newBill.getTransactionNo()==null?"": newBill.getTransactionNo())
                        +"\nTrừ số lượng sản phẩm ");
            }else  if (sellOnRequest.getPaymentType().equals(-2)){
                newBill.setTransactionNo((newBill.getTransactionNo()==null?"": newBill.getTransactionNo())
                        +"\nSet Type : "+newBill.getType()+" -> "+1);
                newBill.setType(1);
            }


       }else {
           return ResponseEntity.badRequest().body("Phương thức thanh toán không đúng !");
       }

        Bill newBillSaved = null;
        try {
            newBillSaved = billService.save(newBill);
            for (BillDetail billDetail : billDetails){
                billDetail.setBill(newBillSaved);
            }


            billDetailService.saveAll(billDetails);
            for (SellOffProductRequest request : sellOffProductRequests){
                cartdetailRepository.deleteByProductDetailIdAndCustomerId(request.getId(),customer.getId());
            }

        }catch (Exception e){
            return ResponseEntity.badRequest().body("Lỗi , Kiểm tra lại hóa đơn : ");

        }
        return ResponseEntity.ok(new MessageResponse(newBillSaved.getId().toString()));

    }
}
