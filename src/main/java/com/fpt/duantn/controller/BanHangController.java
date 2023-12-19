package com.fpt.duantn.controller;

import com.fpt.duantn.domain.Bill;
import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.domain.Employee;
import com.fpt.duantn.domain.Product;
import com.fpt.duantn.dto.DataTablesResponse;
import com.fpt.duantn.dto.ProductBanHangResponse;
import com.fpt.duantn.dto.ProductResponse;
import com.fpt.duantn.service.BillService;
import com.fpt.duantn.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class BanHangController {
    @Autowired
    private ProductService productService;
    @Autowired
    private BillService billService;
    @GetMapping("")
    public String home() {
        return "redirect:/login";
    }
    @GetMapping("/payment/success")
    public String paymentsuccess(@RequestParam("billId") UUID billId,@RequestParam("amount") Double amount,@RequestParam("transactionNo") String transactionNo,Model model) {
        Bill bill = billService.findById(billId).get();

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
        model.addAttribute("bill",bill);
        model.addAttribute("amount",amount/100);
        model.addAttribute("transactionNo",transactionNo);
        return "/admin/view/payment/success";
    }
    @GetMapping("/payment/error")
    public String paymenterror(@RequestParam("billId") UUID billId,@RequestParam("transactionNo") String transactionNo,Model model) {
        Bill bill = billService.findById(billId).get();

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
        model.addAttribute("bill",bill);
        model.addAttribute("transactionNo",transactionNo);
        return "/admin/view/payment/error";
    }

    @GetMapping("/ban-hang/product")
    @ResponseBody
    public DataTablesResponse getProduct(
            @RequestParam(value = "draw", required = false) Optional<Integer> draw,
            @RequestParam(value = "start", required = false) Optional<Integer> start,
            @RequestParam(value = "length", required = false) Optional<Integer> length,
            @RequestParam(value = "search[value]", required = false) Optional<String> searchValue,
            @RequestParam(value = "order[0][column]", required = false) Optional<Integer> orderColumn,
            @RequestParam(value = "order[0][dir]", required = false) Optional<String>  orderDir,
            @RequestParam(value = "categoryId", required = false) Optional<UUID>  categoryId,
            @RequestParam(value = "callAll", required = false) Optional<Integer>  callAll,
            HttpServletRequest request
    ) {
        String orderColumnName = request.getParameter("columns["+orderColumn.orElse(0)+"][data]");
        Pageable pageable = PageRequest.of(start.orElse(0) / length.orElse(10), length.orElse(10));
        List<ProductBanHangResponse> list = productService.searchResponseByKeyAndType(searchValue.orElse(""),categoryId.orElse(null),callAll.isPresent()?null:1);
        DataTablesResponse response = new DataTablesResponse(draw.orElse(10),list.size(),(long)list.size(),list);
        return response;
    }

}
