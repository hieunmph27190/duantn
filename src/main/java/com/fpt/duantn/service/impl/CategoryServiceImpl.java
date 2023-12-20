package com.fpt.duantn.service.impl;

import com.fpt.duantn.domain.Category;

import com.fpt.duantn.repository.CategoryRepository;

import com.fpt.duantn.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<Category> findByType(Integer type, Pageable pageable) {
        return categoryRepository.findByType(type, pageable);
    }


    @Override
    public Page<Category> searchByKeyAndType(String key, Integer type, Pageable pageable) {
        return categoryRepository.searchByKeyAndType(key, type, pageable);
    }

    @Override
    public <S extends Category> List<S> saveAll(Iterable<S> entities) {
        return categoryRepository.saveAll(entities);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findAllById(Iterable<UUID> uuids) {
        return categoryRepository.findAllById(uuids);
    }

    @Override
    public <S extends Category> S save(S entity) {
        return categoryRepository.save(entity);
    }

    @Override
    public Optional<Category> findById(UUID uuid) {
        return categoryRepository.findById(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return categoryRepository.existsById(uuid);
    }

    @Override
    public long count() {
        return categoryRepository.count();
    }

    @Override
    public void deleteById(UUID uuid) {
        categoryRepository.deleteById(uuid);
    }

    @Override
    public void delete(Category entity) {
        categoryRepository.delete(entity);
    }

    @Override
    public Category findByCode(String code) {
        return categoryRepository.findByCode(code).orElse(null);
    }
}
