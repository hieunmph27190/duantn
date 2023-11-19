package com.fpt.duantn.repository;


import com.fpt.duantn.domain.BillDetail;
import com.fpt.duantn.dto.BillDetailReponse;
import com.fpt.duantn.dto.CustomerReponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, UUID> {
    @Query("SELECT new com.fpt.duantn.dto.BillDetailReponse(bd.id,bd.productDetail.product,bd.productDetail,bd.price,bd.quantity,bd.type) from  BillDetail bd " +
            "where ( CAST(bd.id AS string) like :key " +
            "or bd.productDetail.product.name like concat('%',:key,'%') " +
            "or bd.productDetail.size.size like concat('%',:key,'%') " +
            "or bd.productDetail.color.name like concat('%',:key,'%') " +
            "or bd.productDetail.product.id like :key " +
            "or bd.productDetail.id  like :key )" +
            "and (:type is null or bd.type = :type) " +
            "and bd.bill.id = :billId")
    public Page<BillDetailReponse> searchByKeyword(String key , Integer type, UUID billId, Pageable pageable);
}
