package com.fpt.duantn.service.impl;

import com.fpt.duantn.domain.ProductDetail;
import com.fpt.duantn.repository.ProductDetailRepository;

import com.fpt.duantn.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {
    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Override
    public Page<ProductDetail> findByType(Integer type, Pageable pageable) {
        return productDetailRepository.findByType(type, pageable);
    }

    @Override
    public Boolean existsByProductIdAndColorIdAndSizeId(UUID idProduct, UUID idColor, UUID idSize) {
        return productDetailRepository.existsByProductIdAndColorIdAndSizeId(idProduct, idColor, idSize);
    }

    @Override
    public Page<ProductDetail> searchByKeyAndType(String key, Integer type,UUID idProduct, Pageable pageable) {
        return productDetailRepository.searchByKeyAndType(key, type,idProduct, pageable);
    }

    @Override
    public <S extends ProductDetail> List<S> saveAll(Iterable<S> entities) {
        return productDetailRepository.saveAll(entities);
    }

    @Override
    public List<ProductDetail> findAll() {
        return productDetailRepository.findAll();
    }

    @Override
    public List<ProductDetail> findAllById(Iterable<UUID> uuids) {
        return productDetailRepository.findAllById(uuids);
    }

    @Override
    public <S extends ProductDetail> S save(S entity) {
        return productDetailRepository.save(entity);
    }

    @Override
    public Optional<ProductDetail> findById(UUID uuid) {
        return productDetailRepository.findById(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return productDetailRepository.existsById(uuid);
    }

    @Override
    public long count() {
        return productDetailRepository.count();
    }

    @Override
    public void deleteById(UUID uuid) {
        productDetailRepository.deleteById(uuid);
    }

    @Override
    public void delete(ProductDetail entity) {
        productDetailRepository.delete(entity);
    }
}
