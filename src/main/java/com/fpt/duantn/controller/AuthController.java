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
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
<<<<<<< HEAD
@CrossOrigin(origins = "*")
=======
>>>>>>> a8088c38aa590644979ec08768257940a7182c25
@Controller
@RequestMapping()
public class AuthController {
  @GetMapping("/login")
  public String test(Model model){
    return "/admin/login";
  }
}
