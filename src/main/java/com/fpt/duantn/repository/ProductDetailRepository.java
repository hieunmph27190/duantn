package com.fpt.duantn.repository;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Color;
import com.fpt.duantn.domain.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;
@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, UUID> {
    Page<ProductDetail> findByType(Integer type, Pageable pageable);
    @Query("SELECT c from  ProductDetail c where ((:key = '' or CAST(c.id AS string) like :key ) " +
            "or c.color.code like concat('%',:key,'%') " +
            "or c.color.name like concat('%',:key,'%') " +
            "or c.size.code like concat('%',:key,'%') " +
            "or c.size.size like concat('%',:key,'%') )" +
            "and (:type is null or c.type = :type) " +
            "and (:idProduct is not null and c.product.id = :idProduct) ")
    Page<ProductDetail> searchByKeyAndType(@Param("key") String key, @Param("type") Integer type,@Param("idProduct") UUID idProduct, Pageable pageable);
    Boolean existsByProductIdAndColorIdAndSizeId( UUID idProduct, UUID idColor,UUID idSize);
}
