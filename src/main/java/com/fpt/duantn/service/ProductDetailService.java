package com.fpt.duantn.service;

import com.fpt.duantn.domain.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ProductDetailService {
    Page<ProductDetail> findByType(Integer type, Pageable pageable);

    Boolean existsByProductIdAndColorIdAndSizeId(UUID idProduct, UUID idColor, UUID idSize);

    Page<ProductDetail> searchByKeyAndType(String key, Integer type, UUID idProduct, Pageable pageable);

    <S extends ProductDetail> List<S> saveAll(Iterable<S> entities);

    List<ProductDetail> findAll();

    List<ProductDetail> findAllById(Iterable<UUID> uuids);

    <S extends ProductDetail> S save(S entity);

    Optional<ProductDetail> findById(UUID uuid);

    boolean existsById(UUID uuid);

    long count();

    void deleteById(UUID uuid);

    void delete(ProductDetail entity);
}
