package com.fpt.duantn.service;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface BrandService {
    Page<Brand> findByType(Integer type, Pageable pageable);

    Page<Brand> searchByKeyAndType(String key, Integer type, Pageable pageable);

    <S extends Brand> List<S> saveAll(Iterable<S> entities);

    List<Brand> findAll();

    List<Brand> findAllById(Iterable<UUID> uuids);

    <S extends Brand> S save(S entity);

    Optional<Brand> findById(UUID uuid);

    boolean existsById(UUID uuid);

    long count();

    void deleteById(UUID uuid);

    void delete(Brand entity);

    Brand findByCode(String ma);
}
