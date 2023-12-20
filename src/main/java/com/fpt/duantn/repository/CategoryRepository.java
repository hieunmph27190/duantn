package com.fpt.duantn.repository;


import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Category;
import com.fpt.duantn.domain.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Page<Category> findByType(Integer type, Pageable pageable);
    @Query("SELECT c from  Category c where (CAST(c.id AS string) like :key " +
            "or c.code like concat('%',:key,'%') " +
            "or c.name like concat('%',:key,'%')) " +
            "and (:type is null or c.type = :type)")
    Page<Category> searchByKeyAndType(@Param("key") String key, @Param("type") Integer type, Pageable pageable);

    Optional<Category> findByCode(String code);
}
