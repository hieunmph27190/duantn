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
    Page<Image> findByProductIdAndAndProductType(UUID id, @Param("type") Integer type, Pageable pageable);
    List<Image> findByProductIdAndAndProductType(UUID id, @Param("type") Integer type);
    @Query("select i.id from Image i where i.product.id =:id and (:type is null or i.type=:type)")
    List<UUID> findIDByProductId(UUID id, @Param("type") Integer type);
}
