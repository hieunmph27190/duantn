package com.fpt.duantn.repository;


import com.fpt.duantn.domain.Addresss;
import com.fpt.duantn.domain.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, UUID> {

}
