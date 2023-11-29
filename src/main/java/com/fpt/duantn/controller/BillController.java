package com.fpt.duantn.controller;

import com.fpt.duantn.domain.Bill;
import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.domain.Employee;
import com.fpt.duantn.domain.Product;
import com.fpt.duantn.dto.BillReponse;
import com.fpt.duantn.dto.BillUpdateResquest;
import com.fpt.duantn.dto.CustomerReponse;
import com.fpt.duantn.dto.DataTablesResponse;
import com.fpt.duantn.models.User;
import com.fpt.duantn.security.services.AuthenticationService;
import com.fpt.duantn.service.BillService;
import com.fpt.duantn.service.CustomerService;
import com.fpt.duantn.util.FileImgUtil;
import com.fpt.duantn.util.FormErrorUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
@RequestMapping("/bill")
public class BillController {
    @GetMapping("/view")
    public String test(Model model){
        return "/admin/view/bill/bill";
    }
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private BillService billService;

    @GetMapping()
    @ResponseBody
    public DataTablesResponse getBill(
            @RequestParam(value = "draw", required = false) Optional<Integer> draw,
            @RequestParam(value = "start", required = false) Optional<Integer> start,
            @RequestParam(value = "length", required = false) Optional<Integer> length,
            @RequestParam(value = "search[value]", required = false) Optional<String> searchValue,
            @RequestParam(value = "order[0][column]", required = false) Optional<Integer> orderColumn,
            @RequestParam(value = "order[0][dir]", required = false) Optional<String>  orderDir,
            @RequestParam(value = "type", required = false) Optional<Integer> type,
            HttpServletRequest request, Model model
    ) {
        Integer typeInt = type.orElse(-1);
        String orderColumnName = request.getParameter("columns["+orderColumn.orElse(-1)+"][data]");
//        Sort.Order order = new Sort.Order(orderDir.orElse("asc").equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, orderColumnName == null ? "id" : orderColumnName);
//        Sort.Order createDateOrder = new Sort.Order(Sort.Direction.DESC, "createDate");
//        Sort.by(order, createDateOrder);

        Pageable pageable = PageRequest.of(start.orElse(0) / length.orElse(10), length.orElse(10), Sort.by(orderDir.orElse("desc").equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderColumnName == null ? "billCreateDate" : orderColumnName));
        Page<BillReponse> page = billService.searchByKeyword(searchValue.orElse(""),typeInt==-1?null:typeInt, pageable);
        DataTablesResponse response = new DataTablesResponse(draw, page);
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity getBillById(@PathVariable UUID id) {
        if (billService.existsById(id)){
            Bill bill = billService.findById(id).get();

           if (bill.getEmployee()!=null){
               Employee employee =  new Employee();
               employee.setId(bill.getEmployee().getId());
               employee.setName(bill.getEmployee().getName());
               bill.setEmployee(employee);
           }
           if (bill.getCustomer()!=null) {
                Customer customer = new Customer();
                customer.setId(bill.getCustomer().getId());
                customer.setName(bill.getCustomer().getName());
                bill.setCustomer(customer);
           }

           if (bill.getPaymentEmployee()!=null) {
                Employee paymentEmployee = new Employee();
                paymentEmployee.setId(bill.getPaymentEmployee().getId());
                paymentEmployee.setName(bill.getPaymentEmployee().getName());
                bill.setPaymentEmployee(paymentEmployee);
           }

            return ResponseEntity.ok(bill);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping( value = "/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @ModelAttribute BillUpdateResquest billUpdateResquest , BindingResult bindingResult, Authentication authentication) {
        Bill bill = billService.findById(id).orElse(null);
        User user = authenticationService.loadUserByUsername(authentication.getName());
        if (bill==null){
            bindingResult.rejectValue("id","","Id này không tồn tại");
        }
        if (bindingResult.hasErrors()){
            Map errors = FormErrorUtil.changeToMapError(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }
        if (billUpdateResquest.getType()==bill.getType()){

        }else if (billUpdateResquest.getType()==0){
            if (bill.getType()==1||bill.getType()==2){
                bill.setType(0);
            }else {
                return ResponseEntity.badRequest().body("Bill không thể hủy");
            }
        }else if (bill.getType()==0){
            return ResponseEntity.badRequest().body("Bill đã hủy không thể chỉnh sửa thông tin");
        } else if (bill.getType()==7){
            return ResponseEntity.badRequest().body("Bill đã hoàn thành không thể chỉnh sửa thông tin");
        }else if (bill.getType()==1){
            if (billUpdateResquest.getType()==2){
                bill.setEmployee(Employee.builder().id(user.getId()).build());
                bill.setType(2);
            }else {
                return ResponseEntity.badRequest().body("Chỉ có thể chuyển trạng thái thành \"Chờ xác nhận\"");
            }

        }else if (bill.getType()==2){
            if (billUpdateResquest.getType()==3){
                if (bill.getEmployee().equals(Employee.builder().id(user.getId()).build())){
                    bill.setShipeFee(new BigDecimal(billUpdateResquest.getShipeFee()));
                    bill.setType(3);
                }
            }else if (billUpdateResquest.getType()==1){
                bill.setEmployee(null);
                bill.setType(1);
            } else {
                return ResponseEntity.badRequest().body("Chỉ có thể chuyển trạng thái thành \"Đã xác nhận\" hoặc \"Chờ xử lí\"");
            }
        }else if (bill.getType()==3||bill.getType()==4){
            if (billUpdateResquest.getType()==4){
                bill.setType(4);
            }else if (billUpdateResquest.getType()==5){
                bill.setType(5);
            } else {
                return ResponseEntity.badRequest().body("Chỉ có thể chuyển trạng thái thành \"Chờ giao hàng\" hoặc \"Đang giao hàng\"");
            }
        }else if (bill.getType()==5){
            if (billUpdateResquest.getType()==6){
                bill.setType(6);
            }else {
                return ResponseEntity.badRequest().body("Chỉ có thể chuyển trạng thái thành \"Đã hoàn thành\"");
            }
        }
        if (bill.getPaymentEmployee() == null || bill.getPaymentEmployee().equals(Employee.builder().id(user.getId()).build())){
            bill.setPaymentEmployee(Employee.builder().id(user.getId()).build());
            bill.setPaymentAmount(new BigDecimal(billUpdateResquest.getPaymentAmount()));
            bill.setPaymentType(billUpdateResquest.getPaymentType());
            bill.setPaymentTime(new Timestamp(System.currentTimeMillis()));
        }

        bill.setAddress(billUpdateResquest.getAddress());
        bill.setNote(billUpdateResquest.getNote());

        Bill newBill = billService.save(bill);
        if (newBill.getEmployee()!=null){
            Employee employee =  new Employee();
            employee.setId(newBill.getEmployee().getId());
            employee.setName(newBill.getEmployee().getName());
            newBill.setEmployee(employee);
        }
        if (newBill.getCustomer()!=null) {
            Customer customer = new Customer();
            customer.setId(newBill.getCustomer().getId());
            customer.setName(newBill.getCustomer().getName());
            newBill.setCustomer(customer);
        }

        if (newBill.getPaymentEmployee()!=null) {
            Employee paymentEmployee = new Employee();
            paymentEmployee.setId(newBill.getPaymentEmployee().getId());
            paymentEmployee.setName(newBill.getPaymentEmployee().getName());
            newBill.setPaymentEmployee(paymentEmployee);
        }

        return ResponseEntity.ok(newBill);

    }
}