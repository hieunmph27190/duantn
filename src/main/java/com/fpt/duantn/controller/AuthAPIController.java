package com.fpt.duantn.controller;

import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.domain.Employee;
import com.fpt.duantn.domain.Role;
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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
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
    if (customerService.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
    }

    // Create new user's account
    Customer customer = new Customer();
    customer.setEmail(signUpRequest.getEmail());
    customer.setPassword(encoder.encode(signUpRequest.getPassword()));
    customerService.save(customer);
    return ResponseEntity.ok(new MessageResponse("Customer registered successfully!"));
  }

  @PostMapping("/signup/employee")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (employeeService.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
    }


    // Create new user's account
    Employee employee = new Employee();
    employee.setEmail(signUpRequest.getEmail());
    employee.setPassword(encoder.encode(signUpRequest.getPassword()));

    Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

    employee.setRole(modRole);
    employeeService.save(employee);
    return ResponseEntity.ok(new MessageResponse("Employee registered successfully!"));
  }


  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }
}
