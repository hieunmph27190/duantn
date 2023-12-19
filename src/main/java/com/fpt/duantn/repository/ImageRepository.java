package com.fpt.duantn.repository;

import com.fpt.duantn.domain.Image;
import com.fpt.duantn.domain.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {
    Page<Image> findByType(Integer type, Pageable pageable);
    @Query("select new com.fpt.duantn.domain.Image( i.id,i.type)  from Image i where i.product.id =:id and (:type is null or i.product.type=:type)")
    Page<Image> findByProductIdAndProductType(UUID id, @Param("type") Integer type, Pageable pageable);
    @Query("select new com.fpt.duantn.domain.Image( i.id,i.type) from Image i where i.product.id =:id and (:type is null or i.product.type=:type)")
    List<Image> findByProductIdAndProductType(UUID id, @Param("type") Integer type);
    @Query("select i.id from Image i where i.product.id =:id and (:type is null or i.type=:type)")
    List<UUID> findIDByProductId(UUID id, @Param("type") Integer type);
    @Query("select i from Image i where i.product.id =:id and (:type is null or i.type=:type)")
    List<Image> findByProductId(UUID id, @Param("type") Integer type);
}
