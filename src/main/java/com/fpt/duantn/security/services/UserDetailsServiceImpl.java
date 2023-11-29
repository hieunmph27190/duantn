package com.fpt.duantn.security.services;


import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.domain.Employee;
import com.fpt.duantn.models.ERole;
import com.fpt.duantn.models.User;
import com.fpt.duantn.repository.RoleRepository;
import com.fpt.duantn.service.CustomerService;
import com.fpt.duantn.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  CustomerService customerService;
  @Autowired
  EmployeeService employeeService;

  @Autowired
  RoleRepository roleRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Customer> customer = customerService.findByEmail(username);
    Optional<Employee> employee = employeeService.findByEmail(username);
    User user = null;
    if (employee.isPresent()){
       user =new  User().covertFormEmployee(employee.get());
       if (employee.get().getType()==0){
         user.setLocked(true);
       }
    }else if (customer.isPresent()){
      user = new User().covertFormCustomer(customer.get());
      user.setRole(roleRepository.findByName(ERole.ROLE_USER).orElse(null));
      if (customer.get().getType()==0){
        user.setLocked(true);
      }
    }else {
      customer.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

    return UserDetailsImpl.build(user);
  }

}
