package com.fpt.duantn.repository;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Size;
import com.fpt.duantn.domain.Sole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface SoleRepository extends JpaRepository<Sole, UUID> {
    Page<Sole> findByType(Integer type, Pageable pageable);
    @Query("SELECT s from Sole s where (CAST(s.id AS string) like :key " +
            "or s.code like concat('%',:key,'%') " +
            "or s.name like concat('%',:key,'%')) " +
            "and (:type is null or s.type = :type)")
    Page<Sole> searchByKeyAndType(@Param("key") String key, @Param("type") Integer type, Pageable pageable);

    Optional<Sole> findByCode(String code);
}
