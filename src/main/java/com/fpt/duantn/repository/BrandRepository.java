package com.fpt.duantn.repository;

import com.fpt.duantn.domain.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID> {
    Page<Brand> findByType(Integer type, Pageable pageable);
    @Query("SELECT c from  Brand c where (CAST(c.id AS string) like :key " +
            "or c.code like concat('%',:key,'%') " +
            "or c.name like concat('%',:key,'%')) " +
            "and (:type is null or c.type = :type)")
    Page<Brand> searchByKeyAndType(@Param("key") String key, @Param("type") Integer type, Pageable pageable);

    Optional<Brand> findByCode(String code);

}
