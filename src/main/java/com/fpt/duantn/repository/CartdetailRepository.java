package com.fpt.duantn.repository;

import com.fpt.duantn.domain.CartDetail;
import com.fpt.duantn.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartdetailRepository extends JpaRepository<CartDetail,Long> {
    List<CartDetail>findByCustomer(Customer customer);
    List<CartDetail>findByCustomerIdAndType(UUID customerId,Integer type);
    boolean existsByCustomerIdAndProductDetailIdAndType(UUID customerId,UUID productDetailId,Integer type);
    Page<CartDetail> findByCustomerIdAndType(UUID customerId, Integer type, Pageable pageable);
    @Transactional
    @Modifying
    int deleteByCustomer(Customer customer);
    @Transactional
    @Modifying
    int deleteByCartDetailIdAndCustomerId(Long id ,UUID customerId);
    @Transactional
    @Modifying
    int deleteByProductDetailIdAndCustomerId(UUID productDetailID ,UUID customerId);
}
