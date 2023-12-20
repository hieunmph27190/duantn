package com.fpt.duantn.service;

import com.fpt.duantn.domain.Category;
import com.fpt.duantn.domain.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface CategoryService {
    Page<Category> findByType(Integer type, Pageable pageable);

    Page<Category> searchByKeyAndType(String key, Integer type, Pageable pageable);

    <S extends Category> List<S> saveAll(Iterable<S> entities);

    List<Category> findAll();

    List<Category> findAllById(Iterable<UUID> uuids);

    <S extends Category> S save(S entity);

    Optional<Category> findById(UUID uuid);

    boolean existsById(UUID uuid);

    long count();

    void deleteById(UUID uuid);

    void delete(Category entity);

    Category findByCode(String code);
}
