package com.fpt.duantn.repository;


import com.fpt.duantn.domain.Bill;
import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.dto.BillReponse;
import com.fpt.duantn.dto.CustomerReponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<Bill, UUID> {
    @Query("SELECT new com.fpt.duantn.dto.BillReponse(b.id, e.id, e.name, c.id, c.name, b.paymentType, b.billCreateDate, b.shipeFee, b.phoneNumber, b.address, b.type) from Bill b LEFT JOIN b.employee e LEFT JOIN b.paymentEmployee pe LEFT JOIN b.customer c " +
            " where ( CAST(b.id AS string) like :key " +
            "or CAST(e.id AS string) like :key " +
            "or CAST(pe.id AS string) like :key " +
            "or (e.name like concat('%', :key, '%')) " +
            "or b.phoneNumber like concat('%', :key, '%')) " +
            "and (:type is null or b.type = :type)")
    public Page<BillReponse> searchByKeyword(String key, Integer type, Pageable pageable);

    public List<Bill> findByCustomer(Customer customer);
}
