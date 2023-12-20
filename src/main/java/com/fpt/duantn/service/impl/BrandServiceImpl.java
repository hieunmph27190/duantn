package com.fpt.duantn.service.impl;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.repository.BrandRepository;
import com.fpt.duantn.service.BrandService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Page<Brand> findByType(Integer type, Pageable pageable) {
        return brandRepository.findByType(type, pageable);
    }


    @Override
    public Page<Brand> searchByKeyAndType(String key, Integer type, Pageable pageable) {
        return brandRepository.searchByKeyAndType(key, type, pageable);
    }

    @Override
    public <S extends Brand> List<S> saveAll(Iterable<S> entities) {
        return brandRepository.saveAll(entities);
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public List<Brand> findAllById(Iterable<UUID> uuids) {
        return brandRepository.findAllById(uuids);
    }

    @Override
    public <S extends Brand> S save(S entity) {
        return brandRepository.save(entity);
    }

    @Override
    public Optional<Brand> findById(UUID uuid) {
        return brandRepository.findById(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return brandRepository.existsById(uuid);
    }

    @Override
    public long count() {
        return brandRepository.count();
    }

    @Override
    public void deleteById(UUID uuid) {
        brandRepository.deleteById(uuid);
    }

    @Override
    public void delete(Brand entity) {
        brandRepository.delete(entity);
    }

    @Override
    public Brand findByCode(String ma) {
        return brandRepository.findByCode(ma).orElse(null);
    }


}
