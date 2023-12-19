package com.fpt.duantn.controller;

import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.domain.Employee;
import com.fpt.duantn.dto.CustomerReponse;
import com.fpt.duantn.dto.EmployeeReponse;
import com.fpt.duantn.payload.request.SignupRequest;
import com.fpt.duantn.payload.response.MessageResponse;
import com.fpt.duantn.security.services.AuthenticationService;
import com.fpt.duantn.service.CustomerService;
import com.fpt.duantn.service.EmployeeService;
import com.fpt.duantn.service.RoleService;
import com.fpt.duantn.util.FileImgUtil;
import com.fpt.duantn.util.FormErrorUtil;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;


@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("")
    public ResponseEntity getEmployee(Authentication authentication) {
        UUID id =  authenticationService.loadUserByUsername(authentication.getName()).getId();
        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"))){
            if (customerService.existsById(id)){
                ModelMapper modelMapper = new ModelMapper();
                modelMapper.addConverter(new AbstractConverter<Blob, Boolean>() {
                    @Override
                    protected Boolean convert(Blob source) {
                        return source!=null;
                    }
                });
                Customer customer = customerService.findById(id).get();
                CustomerReponse customerReponse = new CustomerReponse();
                modelMapper.map(customer,customerReponse);
                return ResponseEntity.ok(customerReponse);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        if (employeeService.existsById(id)){
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.addConverter(new AbstractConverter<Blob, Boolean>() {
                @Override
                protected Boolean convert(Blob source) {
                    return source!=null;
                }
            });
            Employee employee = employeeService.findById(id).get();
            EmployeeReponse employeeReponse = new EmployeeReponse();
            modelMapper.map(employee,employeeReponse);
            return ResponseEntity.ok(employeeReponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") UUID id, @Valid @ModelAttribute Employee employee, BindingResult bindingResult, @RequestPart(value = "imagesProfile",required = false) MultipartFile[] files,Authentication authentication) {
        UUID idLogin =  authenticationService.loadUserByUsername(authentication.getName()).getId();

        if (bindingResult.hasErrors()){
            Map<String, String> errors = FormErrorUtil.changeToMapError(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }
        id = idLogin;
        employee.setId(id);
        if (files!=null){
            if (files.length>0){
                try {
                    FileImgUtil fileImgUtil = new FileImgUtil();
                    Blob blob =fileImgUtil.convertMultipartFileToBlob(files[0]);
                    if (blob!=null){
                        employeeService.updateEmployeeImage(employee.getId(),blob);
                    }
                } catch (IOException | SQLException e) {
                    return ResponseEntity.badRequest().body("Không đọc ghi được ảnh (kiểm tra lại nhân viên vừa tạo)");
                }
            }
        }
        Employee oldEmployee =  employeeService.findById(employee.getId()).orElse(null);
        if (oldEmployee!=null){
            employee.setEmail(oldEmployee.getEmail());
            employee.setPassword(oldEmployee.getPassword());
            employee.setRole(oldEmployee.getRole());
        }
        employeeService.updateEmployeeWithoutImage(employee);
        Employee employeeSaved = employeeService.findById(employee.getId()).get();
        employeeSaved.setPassword(null);
        return ResponseEntity.ok(employeeSaved);
    }
    @PutMapping("/change-profile-user")
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody SignupRequest signUpRequest, BindingResult bindingResult, Authentication authentication) {
        UUID idLogin =  authenticationService.loadUserByUsername(authentication.getName()).getId();
        if (bindingResult.hasErrors()){
            Map<String, String> errors = FormErrorUtil.changeToMapError(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }
        Customer customer =  customerService.findById(idLogin).orElse(null);
        if (customer==null){
            return ResponseEntity.badRequest().body("Không thể xác định thông tin đăng nhâp");
        }
        customer.setName(signUpRequest.getName());
        customer.setPhoneNumber(signUpRequest.getPhoneNumber());
        customer.setGender(signUpRequest.getGender());
        customer.setCity(signUpRequest.getCity());
        customer.setWard(signUpRequest.getWard());
        customer.setAddress(signUpRequest.getAddress());
        customer.setDistrict(signUpRequest.getDistrict());
        Customer customerSaved = customerService.save(customer);
        return ResponseEntity.ok(new MessageResponse("Customer change profile successfully!"));
    }
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam(required = true) String password,
                                            @RequestParam(required = true) String newPassword,Authentication authentication) {
        UUID idLogin =  authenticationService.loadUserByUsername(authentication.getName()).getId();
        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"))){
            Customer oldCustomer =  customerService.findById(idLogin).orElse(null);
            if (!(password.length()>0&&newPassword.length()>0)){
                return ResponseEntity.badRequest().body("Mật khẩu không được để trống");
            }
            if (oldCustomer!=null){
                if (passwordEncoder.matches(password,oldCustomer.getPassword())){
                    oldCustomer.setPassword(passwordEncoder.encode(newPassword));
                }else {
                    return ResponseEntity.badRequest().body("Mật khẩu không đúng");
                }
            }else {
                return ResponseEntity.badRequest().body("Không thể lấy thông tin đăng nhập");
            }
            customerService.updateCustomerWithoutImage(oldCustomer);
            Customer customerSaved = customerService.findById(oldCustomer.getId()).get();
            customerSaved.setPassword(null);
            return ResponseEntity.ok(customerSaved);
        }else {
            Employee oldEmployee =  employeeService.findById(idLogin).orElse(null);
            if (!(password.length()>0&&newPassword.length()>0)){
                return ResponseEntity.badRequest().body("Mật khẩu không được để trống");
            }
            if (oldEmployee!=null){
                if (passwordEncoder.matches(password,oldEmployee.getPassword())){
                    oldEmployee.setPassword(passwordEncoder.encode(newPassword));
                }else {
                    return ResponseEntity.badRequest().body("Mật khẩu không đúng");
                }
            }else {
                return ResponseEntity.badRequest().body("Không thể lấy thông tin đăng nhập");
            }
            employeeService.updateEmployeeWithoutImage(oldEmployee);
            Employee employeeSaved = employeeService.findById(oldEmployee.getId()).get();
            employeeSaved.setPassword(null);
            return ResponseEntity.ok(employeeSaved);
        }

    }
}
