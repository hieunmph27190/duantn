package com.fpt.duantn.repository;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Color;
import com.fpt.duantn.domain.Product;
import com.fpt.duantn.dto.ProductBanHangResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByType(Integer type, Pageable pageable);
    @Query("SELECT c from  Product c where (CAST(c.id AS string) like :key " +
            "or c.code like concat('%',:key,'%') " +
            "or c.name like concat('%',:key,'%') " +
            "or c.brand.code like concat('%',:key,'%') " +
            "or c.brand.name like concat('%',:key,'%') " +
            "or c.sole.code like concat('%',:key,'%') " +
            "or c.sole.name like concat('%',:key,'%')) " +
            "and (:type is null or c.type = :type)")
    Page<Product> searchByKeyAndType(@Param("key") String key, @Param("type") Integer type, Pageable pageable);



    @Query(value = "select product.id,product.code,product.productname,min(productdetail.price) 'minPrice' ,max(productdetail.price) 'maxPrice' ,images.id 'imageId'  from product \n" +
            " join productdetail on product.id = productdetail.productid \n" +
            "join (SELECT id, productid,type,ROW_NUMBER() OVER (PARTITION BY productid ORDER BY NEWID()) as RowNum \n" +
            "FROM images \n" +
            "where images.type =2 ) images on product.id = images.productid\n" +
            "where images.RowNum =1 \n" +
            "GROUP BY product.id,product.code,product.productname,images.id ", nativeQuery = true)
    Page<ProductBanHangResponse> searchResponseByKeyAndType(@Param("key") String key, @Param("type") Integer type, Pageable pageable);

}
