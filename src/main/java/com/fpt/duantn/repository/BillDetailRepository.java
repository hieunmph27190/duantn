package com.fpt.duantn.repository;


import com.fpt.duantn.domain.BillDetail;
import com.fpt.duantn.dto.BillDetailReponse;
import com.fpt.duantn.dto.CustomerReponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
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

    public List<BillDetail> findByBillIdAndType( UUID billId, Integer type);

    @Query("SELECT sum(bd.price * bd.quantity) as summoney FROM BillDetail bd WHERE (:type IS NULL OR bd.type = :type) AND bd.bill.id = :billId ORDER BY summoney DESC")
    Optional<Double> sumMoneyByBillIdAndType(@Param("billId") UUID billId, @Param("type") Integer type);

    @Query("SELECT sum(bd.quantity) as sumquantity FROM BillDetail bd WHERE (:type IS NULL OR bd.type = :type) AND bd.bill.id = :billId ORDER BY sumquantity DESC")
    Optional<Long> sumQuantityByBillIdAndType(@Param("billId") UUID billId, @Param("type") Integer type);


    List<BillDetail> findByBillId(UUID id);
}
