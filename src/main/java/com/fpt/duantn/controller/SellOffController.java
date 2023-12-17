package com.fpt.duantn.controller;

import com.fpt.duantn.domain.*;
import com.fpt.duantn.dto.SellOffProductRequest;
import com.fpt.duantn.dto.SellOffRequest;
import com.fpt.duantn.models.User;
import com.fpt.duantn.security.services.AuthenticationService;
import com.fpt.duantn.service.*;
import com.fpt.duantn.util.FormErrorUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
@RequestMapping("/selloff")
public class SellOffController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BillService billService;
    @Autowired
    private EmployeeService employeeService;
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
    public ResponseEntity<?> calculateMoney(@ModelAttribute()SellOffRequest sellOffRequest){
        List<SellOffProductRequest> sellOffProductRequests= sellOffRequest.getSanPhams();
        Double sum =0D;
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

    @PostMapping ()
    public ResponseEntity<?> add(@ModelAttribute() SellOffRequest sellOffRequest, Authentication authentication) {
        if (sellOffRequest.getIdKhachHang()==null||sellOffRequest.getThanhToan()==null||sellOffRequest.getTrangThaiTT()==null||sellOffRequest.getSanPhams()==null){
            return ResponseEntity.badRequest().body("Thông tin Không đầy đủ");
        }
        Customer customer = customerService.findById(sellOffRequest.getIdKhachHang()).orElse(null);
        if (customer==null){
            return ResponseEntity.badRequest().body("Thông tin khách hàng không đúng");
        }
        List<SellOffProductRequest>sellOffProductRequests = sellOffRequest.getSanPhams();
        if (sellOffProductRequests.size()<=0){
            return ResponseEntity.badRequest().body("Đơn hàng trống !");
        }
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
            if (request.getQuantity()<=0){
                return ResponseEntity.badRequest().body("Sản phẩm "+request.getId()+" số lượng phải lớn hơn 0");
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
        Employee employeeLogin =  employeeService.findById(user.getId()).orElse(null);
        newBill.setTransactionNo((newBill.getTransactionNo()==null?"": newBill.getTransactionNo()+"\n\n")+employeeLogin.getId()+" : "+employeeLogin.getName() + " : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"))+" Đã Tạo Bill : ");
        newBill.setEmployee(Employee.builder().id(user.getId()).build());
        newBill.setTransactionNo((newBill.getTransactionNo()==null?"": newBill.getTransactionNo())
                +"\nSet Type : "+newBill.getType()+" -> "+3);
        newBill.setType(3);

        newBill.setCustomer(customer);
        newBill.setPhoneNumber(customer.getPhoneNumber());
        newBill.setTransactionNo((newBill.getTransactionNo()==null?"": newBill.getTransactionNo())
                + "\nSet PaymentType : "+newBill.getPaymentType()+" -> "+sellOffRequest.getThanhToan());
        newBill.setPaymentType(sellOffRequest.getThanhToan());

        if (sellOffRequest.getTrangThaiTT().equals(1)){
            newBill.setPaymentEmployee(Employee.builder().id(user.getId()).build());
            newBill.setPaymentTime(new Timestamp(System.currentTimeMillis()));
            newBill.setTransactionNo((newBill.getTransactionNo()==null?"": newBill.getTransactionNo())
                    + "\nSet PaymentAmount : "+newBill.getPaymentAmount()+" -> "+ sum);
            newBill.setPaymentAmount(new BigDecimal(sum));

        }
        newBill.setTransactionNo((newBill.getTransactionNo()==null?"": newBill.getTransactionNo())
                + "\nSet Note : \n"+newBill.getNote()+"\n ----> \n"+sellOffRequest.getNote());
        newBill.setNote(sellOffRequest.getNote());

        Bill newBillSaved = null;
        try {
            newBillSaved = billService.save(newBill);
            for (BillDetail billDetail : billDetails){
                billDetail.setBill(newBillSaved);
            }
            billDetailService.saveAll(billDetails);
        }catch (Exception e){
            return ResponseEntity.ok("Lỗi , Kiểm tra lại hóa đơn");
        }
        return ResponseEntity.ok("Thành công : "+ newBillSaved.getId());
    }
}
