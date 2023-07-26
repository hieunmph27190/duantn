package com.fpt.duantn.repository;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, UUID> {

}
