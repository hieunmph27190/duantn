package com.fpt.duantn.repository;

import com.fpt.duantn.domain.Cart;

import com.fpt.duantn.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByCustomer(Customer customer);

}
