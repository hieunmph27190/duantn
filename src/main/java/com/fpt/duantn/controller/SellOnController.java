package com.fpt.duantn.controller;

import com.fpt.duantn.domain.*;
import com.fpt.duantn.dto.SellOffProductRequest;
import com.fpt.duantn.dto.SellOffRequest;
import com.fpt.duantn.dto.SellOnRequest;
import com.fpt.duantn.models.User;
import com.fpt.duantn.security.services.AuthenticationService;
import com.fpt.duantn.service.BillDetailService;
import com.fpt.duantn.service.BillService;
import com.fpt.duantn.service.CustomerService;
import com.fpt.duantn.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
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


    @GetMapping("/view")
    public String getView (){

        return "/admin/view/selloff/view";
    }

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
               if (productDetail == null||productDetail.getType().equals(0)){
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
        Optional<Double> sumMoney = billDetailService.sumMoneyByBillIdAndType(billID.orElse(null),1);
        return ResponseEntity.ok(sumMoney.orElse(null));
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping ()
    public ResponseEntity<?> add(@RequestBody() SellOnRequest sellOnRequest, Authentication authentication) {

        if (sellOnRequest.getAddress()==null||sellOnRequest.getNote()==null||sellOnRequest.getPhoneNumber()==null||sellOnRequest.getSanPhams()==null) {
            return ResponseEntity.badRequest().body("Thông tin Không đầy đủ");
        }
        List<SellOffProductRequest>sellOffProductRequests = sellOnRequest.getSanPhams();
        List<BillDetail> billDetails = new ArrayList<>();
        Double sum =0D;
        for (SellOffProductRequest request : sellOffProductRequests){
            if (request.getId()==null||request.getQuantity()==null){
                return ResponseEntity.badRequest().body("Thông tin sản phẩm hoặc số lượng bị thiếu");
            }
            ProductDetail productDetail =  productDetailService.findById(request.getId()).orElse(null);
            if (productDetail == null||productDetail.getType().equals(0)){
                return ResponseEntity.badRequest().body("Sản phẩm "+request.getId()+" không tồn tại hoặc đã ngừng kinh doanh");
            }
            if (productDetail.getAmount()<request.getQuantity()){
                return ResponseEntity.badRequest().body("Sản phẩm "+request.getId()+" không đủ số lượng");
            }
            BillDetail billDetail = new BillDetail();
            billDetail.setProductDetail(productDetail);
            billDetail.setPrice(productDetail.getPrice());
            billDetail.setQuantity(request.getQuantity());
            billDetail.setType(1);
            billDetails.add(billDetail);
            sum+=request.getQuantity()*productDetail.getPrice().doubleValue();
        }
        Bill newBill = new Bill();
        User user = authenticationService.loadUserByUsername(authentication.getName());
        Customer customer = new Customer();
        customer.setId(user.getId());
        newBill.setType(1);
        newBill.setCustomer(customer);
        newBill.setPhoneNumber(sellOnRequest.getPhoneNumber());
        newBill.setPaymentType(-1);
        newBill.setNote(sellOnRequest.getNote());
        Bill newBillSaved = null;
        try {
            newBillSaved = billService.save(newBill);
            for (BillDetail billDetail : billDetails){
                billDetail.setBill(newBillSaved);
            }
            billDetailService.saveAll(billDetails);
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.ok("Lỗi , Kiểm tra lại hóa đơn : ");
        }
        return ResponseEntity.ok("Thành công : "+ newBillSaved.getId());
    }
}
