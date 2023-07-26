package com.fpt.duantn.service.impl;

import com.fpt.duantn.domain.Color;
import com.fpt.duantn.domain.Product;
import com.fpt.duantn.repository.ProductRepository;
import com.fpt.duantn.service.ColorService;
import com.fpt.duantn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}
