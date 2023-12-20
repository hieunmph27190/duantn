package com.fpt.duantn.service;

import com.fpt.duantn.domain.Product;
import com.fpt.duantn.domain.Size;
import com.fpt.duantn.dto.ProductBanHangResponse;
import com.fpt.duantn.dto.ProductFilterRequest;
import com.fpt.duantn.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ProductService {
    Page<Product> findByType(Integer type, Pageable pageable);

    Page<Product> searchByKeyAndTypeAndFilter(String key, Integer type, List<UUID> brandIDs, Integer brandSize, List<UUID> categoryIDs, Integer categorySize, List<UUID> soleIDs, Integer soleIDsSize, List<UUID> colorIDs, Integer colorIDsSize, List<UUID> sizeIDs, Integer sizeIDsSize, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    List<ProductBanHangResponse> searchResponseByKeyAndType(String key,UUID categoryId, Integer type);

    Page<Product> searchByKeyAndTypeAndFilter(String key, Integer type, ProductFilterRequest productFilterRequest, Pageable pageable);

    Page<ProductBanHangResponse> searchResponseByKeyAndType(String key, Integer type, Pageable pageable);


    Page<ProductResponse> searchResponseByKeyAndTypeAndFilter(String key, Integer type, Pageable pageable);

    Page<Product> searchByKeyAndType(String key, Integer type, Pageable pageable);

    <S extends Product> List<S> saveAll(Iterable<S> entities);

    List<Product> findAll();

    List<Product> findAllById(Iterable<UUID> uuids);

    <S extends Product> S save(S entity);

    Optional<Product> findById(UUID uuid);

    boolean existsById(UUID uuid);

    long count();

    void deleteById(UUID uuid);

    void delete(Product entity);

    Product findByCode(String code);

}
