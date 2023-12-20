package com.fpt.duantn.service.impl;


import com.fpt.duantn.domain.Product;
import com.fpt.duantn.dto.ProductBanHangResponse;
import com.fpt.duantn.dto.ProductFilterRequest;
import com.fpt.duantn.dto.ProductResponse;
import com.fpt.duantn.repository.ProductRepository;
import com.fpt.duantn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> findByType(Integer type, Pageable pageable) {
        return productRepository.findByType(type, pageable);
    }

    @Override
    public Page<Product> searchByKeyAndTypeAndFilter(String key, Integer type, List<UUID> brandIDs, Integer brandSize, List<UUID> categoryIDs, Integer categorySize, List<UUID> soleIDs, Integer soleIDsSize, List<UUID> colorIDs, Integer colorIDsSize, List<UUID> sizeIDs, Integer sizeIDsSize, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.searchByKeyAndTypeAndFilter(key, type, brandIDs, brandSize, categoryIDs, categorySize, soleIDs, soleIDsSize, colorIDs, colorIDsSize, sizeIDs, sizeIDsSize, minPrice, maxPrice, pageable);
    }
    @Override
    public List<ProductBanHangResponse> searchResponseByKeyAndType(String key,UUID categoryId, Integer type) {
        return productRepository.searchResponseByKeyAndType(key,categoryId, type);
    }

    @Override
    public Page<Product> searchByKeyAndTypeAndFilter(String key, Integer type, ProductFilterRequest productFilterRequest, Pageable pageable) {
        return productRepository.searchByKeyAndTypeAndFilter(key, type,
                productFilterRequest.getBrandIDs(),
                productFilterRequest.getBrandIDs()==null?0:productFilterRequest.getBrandIDs().size(),
                productFilterRequest.getCategoryIDs(),
                productFilterRequest.getCategoryIDs()==null?0:productFilterRequest.getCategoryIDs().size(),
                productFilterRequest.getSoleIDs(),
                productFilterRequest.getSoleIDs()==null?0:productFilterRequest.getSoleIDs().size(),
                productFilterRequest.getColorIDs(),
                productFilterRequest.getColorIDs()==null?0:productFilterRequest.getColorIDs().size(),
                productFilterRequest.getSizeIDs(),
                productFilterRequest.getSizeIDs()==null?0:productFilterRequest.getSizeIDs().size(),
                productFilterRequest.getMinPrice(),
                productFilterRequest.getMaxPrice(),
                pageable);
    }

    @Override
    public Page<ProductBanHangResponse> searchResponseByKeyAndType(String key, Integer type, Pageable pageable) {
        return productRepository.searchResponseByKeyAndType(key, type, pageable);
    }


    @Override
    public Page<ProductResponse> searchResponseByKeyAndTypeAndFilter(String key, Integer type, Pageable pageable) {
        return productRepository.searchResponseByKeyAndTypeAndFilter(key, type, pageable);
    }

    @Override
    public Page<Product> searchByKeyAndType(String key, Integer type, Pageable pageable) {
        return productRepository.searchByKeyAndType(key, type, pageable);
    }

    @Override
    public <S extends Product> List<S> saveAll(Iterable<S> entities) {
        return productRepository.saveAll(entities);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAllById(Iterable<UUID> uuids) {
        return productRepository.findAllById(uuids);
    }

    @Override
    public <S extends Product> S save(S entity) {
        return productRepository.save(entity);
    }

    @Override
    public Optional<Product> findById(UUID uuid) {
        return productRepository.findById(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return productRepository.existsById(uuid);
    }

    @Override
    public long count() {
        return productRepository.count();
    }

    @Override
    public void deleteById(UUID uuid) {
        productRepository.deleteById(uuid);
    }

    @Override
    public void delete(Product entity) {
        productRepository.delete(entity);
    }

    @Override
    public Product findByCode(String code) {
        return productRepository.findByCode(code).orElse(null);
    }
}
