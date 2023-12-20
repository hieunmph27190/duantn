package com.fpt.duantn.controller;

import com.fpt.duantn.domain.*;
import com.fpt.duantn.dto.*;
import com.fpt.duantn.models.User;
import com.fpt.duantn.payload.response.MessageResponse;
import com.fpt.duantn.security.services.AuthenticationService;
import com.fpt.duantn.service.*;
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
import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
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
    @Autowired
    private BillDetailService billDetailService;
    @Autowired
    private ProductDetailService productDetailService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CustomerService customerService;

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping()
    @ResponseBody
    public DataTablesResponse getBill(
            @RequestParam(value = "draw", required = false) Optional<Integer> draw,
            @RequestParam(value = "start", required = false) Optional<Integer> start,
            @RequestParam(value = "length", required = false) Optional<Integer> length,
            @RequestParam(value = "search[value]", required = false) Optional<String> searchValue,
            @RequestParam(value = "order[0][column]", required = false) Optional<Integer> orderColumn,
            @RequestParam(value = "order[0][dir]", required = false) Optional<String>  orderDir,
            @RequestParam(value = "phoneNumber", required = false) Optional<String> phoneNumber,
            @RequestParam(value = "startTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> startTime,
            @RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> endTime,
            @RequestParam(value = "paymentType", required = false) Optional<Integer> paymentType,
            @RequestParam(value = "type", required = false) Optional<Integer> type,
            HttpServletRequest request, Model model, Authentication authentication
    ) {

        User user = authenticationService.loadUserByUsername(authentication.getName());
        UUID employeeId = user.getId();
        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))){
            employeeId = null;
        }

        Timestamp timestampStart = null;
        if (startTime.orElse(null)!=null){
            timestampStart = Timestamp.valueOf(startTime.orElse(null));
        }
        Timestamp timestampEnd = null;
        if (endTime.orElse(null)!=null){
            timestampEnd = Timestamp.valueOf(endTime.orElse(null));
        }

        Integer typeInt = type.orElse(-1);
        Integer paymentTypeInt = paymentType.orElse(-1);
        String orderColumnName = request.getParameter("columns["+orderColumn.orElse(-1)+"][data]");
//        Sort.Order order = new Sort.Order(orderDir.orElse("asc").equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, orderColumnName == null ? "id" : orderColumnName);
//        Sort.Order createDateOrder = new Sort.Order(Sort.Direction.DESC, "createDate");
//        Sort.by(order, createDateOrder);
        String phoneNumberx = null;
        if (phoneNumber.isPresent()){
            phoneNumberx= phoneNumber.get().equals("")?null:phoneNumber.get();
        }
        Pageable pageable = PageRequest.of(start.orElse(0) / length.orElse(10), length.orElse(10), Sort.by(orderDir.orElse("desc").equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderColumnName == null ? "billCreateDate" : orderColumnName));
        Page<BillReponse> page = billService.searchByKeyword(searchValue.orElse(""),phoneNumberx,timestampStart,timestampEnd,paymentTypeInt==-1?null:paymentTypeInt,typeInt==-1?null:typeInt,null, pageable);
        DataTablesResponse response = new DataTablesResponse(draw, page);
        return response;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/sellon")
    @ResponseBody
    public DataTablesResponse getBillSellOn(
            @RequestParam(value = "draw", required = false) Optional<Integer> draw,
            @RequestParam(value = "start", required = false) Optional<Integer> start,
            @RequestParam(value = "length", required = false) Optional<Integer> length,
            @RequestParam(value = "search[value]", required = false) Optional<String> searchValue,
            @RequestParam(value = "order[0][column]", required = false) Optional<Integer> orderColumn,
            @RequestParam(value = "order[0][dir]", required = false) Optional<String>  orderDir,
            @RequestParam(value = "type", required = false) Optional<Integer> type,
            HttpServletRequest request, Authentication authentication
    ) {
        User user = authenticationService.loadUserByUsername(authentication.getName());
        Integer typeInt = type.orElse(-1);
        String orderColumnName = request.getParameter("columns["+orderColumn.orElse(-1)+"][data]");
        Pageable pageable = PageRequest.of(start.orElse(0) / length.orElse(10), length.orElse(10), Sort.by(orderDir.orElse("desc").equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderColumnName == null ? "billCreateDate" : orderColumnName));
        Page<BillSellOnReponse> page = billService.searchByKeyword(user.getId(),typeInt==-1?null:typeInt, pageable);
        DataTablesResponse response = new DataTablesResponse(draw, page);
        return response;
    }
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cancel-bill/{id}")
    public ResponseEntity huyBill(@PathVariable UUID id,Authentication authentication) {
        if (billService.existsById(id)){
            Bill bill = billService.findById(id).get();
            User user = authenticationService.loadUserByUsername(authentication.getName());
            Customer customer  =  customerService.findById(user.getId()).orElse(null);
            if (customer==null){
                return ResponseEntity.badRequest().body("Không lấy được thông tin đăng nhập");
            }
            bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo()+"\n\n")+customer.getId()+" : "+customer.getName() + " : USER : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")));
            if (user.getId().toString().equals(bill.getCustomer().getId().toString())){
                if (bill.getType()==1){
                    if (bill.getPaymentAmount()==null||bill.getPaymentAmount().doubleValue()==0){
                        bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                                +"\nSet Type : "+bill.getType()+" -> "+0);
                        bill.setType(0);
                        billService.save(bill);
                        return ResponseEntity.ok(new MessageResponse("Hủy đơn thành công"));
                    }else {
                        return ResponseEntity.badRequest().body("Đơn hàng đã thanh toán không thể hủy");
                    }
                }else if (bill.getType()==-2){
                    if (bill.getPaymentAmount()==null||bill.getPaymentAmount().doubleValue()==0){
                        List<BillDetail> billDetails = billDetailService.findByBillIdAndType(bill.getId(),1);
                        List<ProductDetail> productDetails2  = new ArrayList<>();
                        for (BillDetail billDetail:billDetails) {
                            ProductDetail productDetail = billDetail.getProductDetail();
                            productDetail.setAmount(productDetail.getAmount()+billDetail.getQuantity());
                            productDetails2.add(productDetail);
                        }
                        bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                                + "\nCộng lại số lượng vào sản phẩm");
                        bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                                +"\nSet Type : "+bill.getType()+" -> "+0);
                        bill.setType(0);
                        productDetailService.saveAll(productDetails2);
                        billService.save(bill);
                        return ResponseEntity.ok(new MessageResponse("Hủy đơn thành công"));
                    }else {
                        return ResponseEntity.badRequest().body("Đơn hàng đã thanh toán không thể hủy");
                    }
                } else {
                    return ResponseEntity.badRequest().body("Chỉ có thể hủy khi đơn Chờ xử lí hoặc chờ thanh toán !");
                }
            }else {
                return ResponseEntity.badRequest().body("Bạn không có quyền hủy đơn này");
            }

        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PreAuthorize("hasRole('USER')")
    @PutMapping("/nhan-hang/{id}")
    public ResponseEntity nhanHang(@PathVariable UUID id,Authentication authentication) {
        if (billService.existsById(id)){
            Bill bill = billService.findById(id).get();
            User user = authenticationService.loadUserByUsername(authentication.getName());
            Customer customer  =  customerService.findById(user.getId()).orElse(null);
            if (customer==null){
                return ResponseEntity.badRequest().body("Không lấy được thông tin đăng nhập");
            }
            bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo()+"\n\n")+customer.getId()+" : "+customer.getName() + " : USER : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")));
            if (user.getId().toString().equals(bill.getCustomer().getId().toString())){
                if (bill.getType()==5){

                    bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                            +"\nSet Type : "+bill.getType()+" -> "+6);
                    bill.setType(6);
                    billService.save(bill);
                    return ResponseEntity.ok(new MessageResponse("Đã nhận hàng thành công"));

                } else {
                    return ResponseEntity.badRequest().body("Chỉ có thể hủy khi đơn Chờ xử lí hoặc chờ thanh toán !");
                }
            }else {
                return ResponseEntity.badRequest().body("Bạn không có quyền hủy đơn này");
            }

        } else {
            return ResponseEntity.notFound().build();
        }
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

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @PutMapping( value = "/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @ModelAttribute BillUpdateResquest billUpdateResquest , BindingResult bindingResult, Authentication authentication) {
        Bill bill = billService.findById(id).orElse(null);
        User user = authenticationService.loadUserByUsername(authentication.getName());
        List<ProductDetail> productDetails = new ArrayList<>();
        if (bindingResult.hasErrors()){
            Map errors = FormErrorUtil.changeToMapError(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }
        if (bill==null){
            bindingResult.rejectValue("id","","Id này không tồn tại");
        }

        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))){
            Employee employeeLogin =  employeeService.findById(user.getId()).orElse(null);
            bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo()+"\n\n")+employeeLogin.getId()+" : "+employeeLogin.getName() + " : ADMIN : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")));
            if (billUpdateResquest.getPaymentAmount().doubleValue()<0||billUpdateResquest.getPaymentAmount().doubleValue() % 1 != 0){
                return ResponseEntity.badRequest().body("PaymentAmount không hợp lệ !");
            }

            if (!(bill.getPaymentAmount().doubleValue()==billUpdateResquest.getPaymentAmount().doubleValue())){
                bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                        + "\nSet PaymentAmount : "+bill.getPaymentAmount()+" -> "+billUpdateResquest.getPaymentAmount());
                bill.setPaymentEmployee(Employee.builder().id(user.getId()).build());
                bill.setPaymentAmount(new BigDecimal(billUpdateResquest.getPaymentAmount()));
                bill.setPaymentTime(new Timestamp(System.currentTimeMillis()));
            }

            Double total = billDetailService.sumMoneyByBillIdAndType(bill.getId(),null).orElse(null);
            if (total==null){
                return ResponseEntity.badRequest().body("Lỗi tính tổng tiền !");
            }
            if((bill.getPaymentAmount().doubleValue()<bill.getShipeFee().doubleValue()+total)&&((bill.getPaymentType().intValue()!=(-2)&&billUpdateResquest.getType().intValue()>=4)||(bill.getPaymentType().equals(-2)&&billUpdateResquest.getType().intValue()>=7))){
                return ResponseEntity.badRequest().body("Số tiền thanh toán không đủ !");
            }

            if (!((bill.getAddress()==null?"":bill.getAddress()).equals(billUpdateResquest.getAddress()))){
                bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                                + "\nSet Address : "+bill.getAddress()+" -> "+billUpdateResquest.getAddress());
                bill.setAddress(billUpdateResquest.getAddress());
            }
            if (!((bill.getNote()==null?"":bill.getNote()).equals(billUpdateResquest.getNote()))){
                bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                        + "\nSet Note : \n"+bill.getNote()+"\n ----> \n"+billUpdateResquest.getNote());
                bill.setNote(billUpdateResquest.getNote());
            }


            if ((bill.getType()>=0&&bill.getType()<4)&&((billUpdateResquest.getType()>=4)||(billUpdateResquest.getType()==-2))){
                List<BillDetail> billDetails = billDetailService.findByBillIdAndType(bill.getId(),1);
                List<ProductDetail> productDetails2  = new ArrayList<>();
                for (BillDetail billDetail:billDetails) {

                    ProductDetail productDetail = billDetail.getProductDetail();
                    if (productDetail == null){
                        return ResponseEntity.badRequest().body("Sản phẩm "+productDetail.getId()+" không tồn tại");
                    }
                    if (productDetail.getAmount()<billDetail.getQuantity()){
                        return ResponseEntity.badRequest().body("Sản phẩm "+productDetail.getId()+" không đủ số lượng");
                    }
                    if (billDetail.getQuantity()<=0){
                        return ResponseEntity.badRequest().body("Sản phẩm "+productDetail.getId()+" số lượng phải lớn hơn 0");
                    }
                    productDetail.setAmount(productDetail.getAmount()-billDetail.getQuantity());
                    productDetails2.add(productDetail);
                }
                bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                        + "\nTrừ số lượng sản phẩm");

                productDetailService.saveAll(productDetails2);
            }
            if ((billUpdateResquest.getType()>=0&&billUpdateResquest.getType()<4)&&((bill.getType()>=4)||(bill.getType()==-2))){
                List<BillDetail> billDetails = billDetailService.findByBillIdAndType(bill.getId(),1);
                List<ProductDetail> productDetails2  = new ArrayList<>();
                for (BillDetail billDetail:billDetails) {

                    ProductDetail productDetail = billDetail.getProductDetail();

                    productDetail.setAmount(productDetail.getAmount()+billDetail.getQuantity());
                    productDetails2.add(productDetail);
                }
                bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                        + "\nCộng lại số lượng vào sản phẩm");
                productDetailService.saveAll(productDetails2);
            }

            if (!(bill.getType().equals(billUpdateResquest.getType()))){
                bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                        +"\nSet Type : "+bill.getType()+" -> "+billUpdateResquest.getType());
                bill.setType(billUpdateResquest.getType());
            }


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

        }else if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MODERATOR"))){

            Employee employeeLogin =  employeeService.findById(user.getId()).orElse(null);
            bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo()+"\n\n")+employeeLogin.getId()+" : "+employeeLogin.getName() + " : MODERATOR : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")));
            if (billUpdateResquest.getPaymentAmount().doubleValue()<0||billUpdateResquest.getPaymentAmount().doubleValue() % 1 != 0){
                return ResponseEntity.badRequest().body("PaymentAmount không hợp lệ !");
            }
           if (bill.getType()==-2){
               if (billUpdateResquest.getType().intValue()==0){
                   bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                           +"\nSet Type : "+bill.getType()+" -> "+billUpdateResquest.getType());
                   bill.setType(0);
                   List<BillDetail> billDetails = billDetailService.findByBillIdAndType(bill.getId(),1);
                   List<ProductDetail> productDetails2  = new ArrayList<>();
                   for (BillDetail billDetail:billDetails) {
                       ProductDetail productDetail = billDetail.getProductDetail();
                       productDetail.setAmount(productDetail.getAmount()+billDetail.getQuantity());
                       productDetails2.add(productDetail);
                   }
                   if (!((bill.getAddress()==null?"":bill.getAddress()).equals(billUpdateResquest.getAddress()))){
                       bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                               + "\nSet Address : "+bill.getAddress()+" -> "+billUpdateResquest.getAddress());
                       bill.setAddress(billUpdateResquest.getAddress());
                   }
                   if (!((bill.getNote()==null?"":bill.getNote()).equals(billUpdateResquest.getNote()))){
                       bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                               + "\nSet Note : \n"+bill.getNote()+"\n ----> \n"+billUpdateResquest.getNote());
                       bill.setNote(billUpdateResquest.getNote());
                   }

                   Bill newBillSaved = null;
                   try {
                       productDetailService.saveAll(productDetails2);
                       bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                               + "\nĐã hoàn lại sản phẩm !");
                       newBillSaved = billService.save(bill);
                       for (BillDetail billDetail : billDetails){
                           billDetail.setBill(newBillSaved);
                       }
                       billDetailService.saveAll(billDetails);
                       return ResponseEntity.ok("Thành công , Đã hoàn lại sản phẩm");
                   }catch (Exception e){
                       return ResponseEntity.ok("Lỗi , Kiểm tra lại hóa đơn");
                   }
               }else if (billUpdateResquest.getType().intValue()==-2){

                   if ((bill.getPaymentEmployee() == null || bill.getPaymentEmployee().getId().toString().equals(user.getId().toString()))
                           &&(bill.getPaymentAmount().doubleValue()!=billUpdateResquest.getPaymentAmount())){
                       bill.setPaymentEmployee(Employee.builder().id(user.getId()).build());
                       bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                               + "\nSet PaymentAmount : "+bill.getPaymentAmount()+" -> "+billUpdateResquest.getPaymentAmount());
                       bill.setPaymentAmount(new BigDecimal(billUpdateResquest.getPaymentAmount()));
                       bill.setPaymentTime(new Timestamp(System.currentTimeMillis()));
                   }

                   if (!((bill.getAddress()==null?"":bill.getAddress()).equals(billUpdateResquest.getAddress()))){
                       bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                               + "\nSet Address : "+bill.getAddress()+" -> "+billUpdateResquest.getAddress());
                       bill.setAddress(billUpdateResquest.getAddress());
                   }
                   if (!((bill.getNote()==null?"":bill.getNote()).equals(billUpdateResquest.getNote()))){
                       bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                               + "\nSet Note : \n"+bill.getNote()+"\n ----> \n"+billUpdateResquest.getNote());
                       bill.setNote(billUpdateResquest.getNote());
                   }
               } else {
                   return ResponseEntity.badRequest().body("Đơn hàng này chỉ có thể chuyển sang trọng thái Hủy !");
               }
           }


            if (billUpdateResquest.getType().intValue()==0){
                if (bill.getType()==1||bill.getType()==2){
                    if (bill.getPaymentAmount()==null||bill.getPaymentAmount().doubleValue()==0){
                        bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                                +"\nSet Type : "+bill.getType()+" -> "+billUpdateResquest.getType());
                        bill.setType(billUpdateResquest.getType());
                    }else {
                        return ResponseEntity.badRequest().body("Đơn hàng đã thanh toán không thể hủy ! Liên hệ hỗ trọ viên để được hỗ trợ !");
                    }
                }else {
                    return ResponseEntity.badRequest().body("Bill không thể hủy");
                }
            }else if (bill.getType()==0){
                return ResponseEntity.badRequest().body("Bill đã hủy không thể chỉnh sửa thông tin");
            } else if (bill.getType()==7){
                return ResponseEntity.badRequest().body("Bill đã hoàn thành không thể chỉnh sửa thông tin");
            }else if (bill.getType()==1){
                if(billUpdateResquest.getType()==1){

                } else if (billUpdateResquest.getType()==2){
                    bill.setEmployee(Employee.builder().id(user.getId()).build());
                    bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                            +"\nSet Type : "+bill.getType()+" -> "+billUpdateResquest.getType());
                    bill.setType(billUpdateResquest.getType());
                }else {
                    return ResponseEntity.badRequest().body("Chỉ có thể chuyển trạng thái thành \"Chờ xác nhận\"");
                }

            }else if (bill.getType()==2){
                if (billUpdateResquest.getType()==2){

                } else if (billUpdateResquest.getType()==3){
                    if (bill.getEmployee().getId().toString().equals(user.getId().toString())){

                        bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                                +"\nSet Type : "+bill.getType()+" -> "+billUpdateResquest.getType());
                        bill.setType(billUpdateResquest.getType());
                    }
                }else if (billUpdateResquest.getType()==1){
                    bill.setEmployee(null);
                    bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                            +"\nSet Type : "+bill.getType()+" -> "+billUpdateResquest.getType());
                    bill.setType(billUpdateResquest.getType());
                } else {
                    return ResponseEntity.badRequest().body("Chỉ có thể chuyển trạng thái thành \"Đã xác nhận\" hoặc \"Chờ xử lí\"");
                }
            }else if (bill.getType()==3){

                if(billUpdateResquest.getType()==3){

                } else if (billUpdateResquest.getType()==4) {


                    List<BillDetail> billDetails =  billDetailService.findByBillIdAndType(bill.getId(),1);

                        for (BillDetail billDetail:billDetails) {
                            ProductDetail productDetail = billDetail.getProductDetail();
                            if (productDetail == null){
                                return ResponseEntity.badRequest().body("Sản phẩm "+productDetail.getId()+" không tồn tại hoặc đã ngừng kinh doanh");
                            }
                            if (productDetail.getAmount()<billDetail.getQuantity()){
                                return ResponseEntity.badRequest().body("Sản phẩm "+productDetail.getId()+" không đủ số lượng");
                            }
                            if (billDetail.getQuantity()<=0){
                                return ResponseEntity.badRequest().body("Sản phẩm "+productDetail.getId()+" số lượng phải lớn hơn 0");
                            }
                            productDetail.setAmount(productDetail.getAmount()- billDetail.getQuantity());
                            productDetails.add(productDetail);
                        }



                    bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                            +"\nSet Type : "+bill.getType()+" -> "+billUpdateResquest.getType());
                    bill.setType(billUpdateResquest.getType());
                }else {
                    return ResponseEntity.badRequest().body("Chỉ có thể chuyển trạng thái thành \"Chờ giao hàng\" ");
                }
            }else if (bill.getType()==4){
                if (billUpdateResquest.getType()==4){

                }else if (billUpdateResquest.getType()==5){
                    bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                            +"\nSet Type : "+bill.getType()+" -> "+billUpdateResquest.getType());
                    bill.setType(billUpdateResquest.getType());
                } else {
                    return ResponseEntity.badRequest().body("Chỉ có thể chuyển trạng thái thành \"Chờ giao hàng\" hoặc \"Đang giao hàng\"");
                }
            }else if (bill.getType()==5){
                if (billUpdateResquest.getType()==5){

                } else if (billUpdateResquest.getType()==6||billUpdateResquest.getType()==7){
                    bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                            +"\nSet Type : "+bill.getType()+" -> "+billUpdateResquest.getType());
                    bill.setType(billUpdateResquest.getType());
                }else {
                    return ResponseEntity.badRequest().body("Chỉ có thể chuyển trạng thái thành \"Đã nhận hàng\" / \"Đã hoàn thành\"");
                }
            }else if (bill.getType()==6){
                if (billUpdateResquest.getType()==6){

                } else if (billUpdateResquest.getType()==7){
                    bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                            +"\nSet Type : "+bill.getType()+" -> "+billUpdateResquest.getType());
                    bill.setType(billUpdateResquest.getType());
                }else {
                    return ResponseEntity.badRequest().body("Chỉ có thể chuyển trạng thái thành  \"Đã hoàn thành\"");
                }
            }
            if ((bill.getPaymentEmployee() == null || bill.getPaymentEmployee().getId().toString().equals(user.getId().toString()))
                    &&(bill.getPaymentAmount().doubleValue()!=billUpdateResquest.getPaymentAmount())){
                bill.setPaymentEmployee(Employee.builder().id(user.getId()).build());
                bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                        + "\nSet PaymentAmount : "+bill.getPaymentAmount()+" -> "+billUpdateResquest.getPaymentAmount());
                bill.setPaymentAmount(new BigDecimal(billUpdateResquest.getPaymentAmount()));
                bill.setPaymentTime(new Timestamp(System.currentTimeMillis()));
            }
            Double total = billDetailService.sumMoneyByBillIdAndType(bill.getId(),null).orElse(null);
            if (total==null){
                return ResponseEntity.badRequest().body("Lỗi tính tổng tiền !");
            }
            if((bill.getPaymentAmount().doubleValue()<bill.getShipeFee().doubleValue()+total)&&((bill.getPaymentType().intValue()!=(-2)&&billUpdateResquest.getType().intValue()>=4)||(bill.getPaymentType().equals(-2)&&billUpdateResquest.getType().intValue()>=7))){
                return ResponseEntity.badRequest().body("Số tiền thanh toán không đủ !");
            }


            if (!((bill.getAddress()==null?"":bill.getAddress()).equals(billUpdateResquest.getAddress()))){
                bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                        + "\nSet Address : "+bill.getAddress()+" -> "+billUpdateResquest.getAddress());
                bill.setAddress(billUpdateResquest.getAddress());
            }
            if (!((bill.getNote()==null?"":bill.getNote()).equals(billUpdateResquest.getNote()))){
                bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                        + "\nSet Note : \n"+bill.getNote()+"\n ----> \n"+billUpdateResquest.getNote());
                bill.setNote(billUpdateResquest.getNote());
            }
            if (productDetails.size()>0){
                productDetailService.saveAll(productDetails);
                bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                        + "\nĐã trừ số lượng trong sản phẩm !");
            }
            Bill newBill = billService.save(bill);

            return ResponseEntity.ok("Thành công !");

        }

        return ResponseEntity.badRequest().body("Yêu cầu quyền ! ");


    }
}