package com.fpt.duantn.controller;

import com.fpt.duantn.domain.Bill;
import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.domain.Employee;
import com.fpt.duantn.dto.BillReponse;
import com.fpt.duantn.dto.CustomerReponse;
import com.fpt.duantn.dto.DataTablesResponse;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/bill")
public class BillController {
    @GetMapping("/view")
    public String test(Model model){
        return "/admin/view/bill/bill";
    }

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



}