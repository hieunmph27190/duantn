package com.fpt.duantn.models;

import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.domain.Employee;
import com.fpt.duantn.domain.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  private UUID id;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  private Boolean locked;

  private Role role ;

  public User covertFormEmployee(Employee employee){
    return User.builder().email(employee.getEmail()).id(employee.getId()).password(employee.getPassword()).role(employee.getRole()).build();
  }
  public User covertFormCustomer(Customer customer){
    return User.builder().email(customer.getEmail()).id(customer.getId()).password(customer.getPassword()).role(null).build();
  }


}
