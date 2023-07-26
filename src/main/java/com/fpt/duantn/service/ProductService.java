package com.fpt.duantn.service;

import com.fpt.duantn.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ProductService {
    Page<Product> findByType(Integer type, Pageable pageable);

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
}
