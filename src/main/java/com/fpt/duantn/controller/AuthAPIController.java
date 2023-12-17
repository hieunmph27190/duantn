package com.fpt.duantn.controller;

import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.domain.Employee;
import com.fpt.duantn.domain.Role;
import com.fpt.duantn.dto.MailInfo;
import com.fpt.duantn.models.ERole;
import com.fpt.duantn.payload.request.LoginRequest;
import com.fpt.duantn.payload.request.SignupRequest;
import com.fpt.duantn.payload.response.MessageResponse;
import com.fpt.duantn.payload.response.UserInfoResponse;
import com.fpt.duantn.repository.RoleRepository;
import com.fpt.duantn.security.jwt.JwtUtils;
import com.fpt.duantn.security.services.UserDetailsImpl;
import com.fpt.duantn.service.CustomerService;
import com.fpt.duantn.service.EmployeeService;
import com.fpt.duantn.service.SendMailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/auth")
public class AuthAPIController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  CustomerService customerService;
  @Autowired
  EmployeeService employeeService;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  SendMailService sendMail;


  @PostMapping(value = "/signin",consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    if (Boolean.TRUE.equals(userDetails.getLocked())){
      return ResponseEntity.badRequest().body("Tài khoản này đã bị khóa");
    }

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(new UserInfoResponse(userDetails.getId(),
                                   userDetails.getUsername(),
                                   roles));
  }

  @PostMapping("/signup/customer")
  public ResponseEntity<?> registerCustomer(@Valid @RequestBody SignupRequest signUpRequest) {
    if (employeeService.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
    }
    boolean tkCDK = false;
    Customer customerDB = customerService.findCByPhoneNumber(signUpRequest.getPhoneNumber()).orElse(null);
    Customer customer = new Customer();
    if (customerDB==null){

    }else {
      if (customerDB.getType()!=2){
        return ResponseEntity.badRequest().body(new MessageResponse("Error: PhoneNumber is already taken!"));
      }else {
        tkCDK =true;
        customer = customerDB;
      }
    }
    // Create new user's account
    customer.setEmail(signUpRequest.getEmail());
    customer.setName(signUpRequest.getName());
    customer.setPhoneNumber(signUpRequest.getPhoneNumber());
    customer.setGender(signUpRequest.getGender());
    customer.setCity(signUpRequest.getCity());
    customer.setWard(signUpRequest.getWard());
    customer.setAddress(signUpRequest.getAddress());
    customer.setDistrict(signUpRequest.getDistrict());
    String randomPassword = generateRandomPassword(10);
    customer.setPassword(encoder.encode(randomPassword));
    customer.setType(1);
    try {
      sendMailPassword(customer.getEmail(),randomPassword,"");
    } catch (IOException|MessagingException e ) {
      return ResponseEntity.badRequest().body(new MessageResponse("Không thể gửi email"));
    }
    Customer customerSaved = customerService.save(customer);
    return ResponseEntity.ok(new MessageResponse("Customer registered successfully!"));
  }

//  @PostMapping("/signup/employee")
//  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
//    if (customerService.existsByEmail(signUpRequest.getEmail())||employeeService.existsByEmail(signUpRequest.getEmail())) {
//      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
//    }
//
//
//    // Create new user's account
//    Employee employee = new Employee();
//    employee.setEmail(signUpRequest.getEmail());
//
//    Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
//            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//
//    employee.setRole(modRole);
//    employeeService.save(employee);
//    return ResponseEntity.ok(new MessageResponse("Employee registered successfully!"));
//  }


  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }


  public static String generateRandomPassword(int length) {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    Random random = new SecureRandom();
    return random.ints(length, 0, characters.length())
            .mapToObj(characters::charAt)
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .toString();
  }


  // sendmail
  public void sendMailPassword(String email, String password, String title) throws IOException, MessagingException {
    String body = "<div>\r\n" + "        <h3>Mật khẩu của bạn là: <span style=\"color:red; font-weight: bold;\">"
            + password + "</span></h3>\r\n" + "    </div>";
    sendMail.send( new MailInfo(email,title,body));
  }




}
