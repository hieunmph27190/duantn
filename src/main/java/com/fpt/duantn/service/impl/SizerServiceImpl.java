package com.fpt.duantn.service.impl;


import com.fpt.duantn.domain.Size;
import com.fpt.duantn.repository.SizeRepository;
import com.fpt.duantn.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SizerServiceImpl implements SizeService {
    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public Page<Size> findByType(Integer type, Pageable pageable) {
        return sizeRepository.findByType(type, pageable);
    }


    @Override
    public Page<Size> searchByKeyAndType(String key, Integer type, Pageable pageable) {
        return sizeRepository.searchByKeyAndType(key, type, pageable);
    }

    @Override
    public <S extends Size> List<S> saveAll(Iterable<S> entities) {
        return sizeRepository.saveAll(entities);
    }

    @Override
    public List<Size> findAll() {
        return sizeRepository.findAll();
    }

    @Override
    public List<Size> findAllById(Iterable<UUID> uuids) {
        return sizeRepository.findAllById(uuids);
    }

    @Override
    public <S extends Size> S save(S entity) {
        return sizeRepository.save(entity);
    }

    @Override
    public Optional<Size> findById(UUID uuid) {
        return sizeRepository.findById(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return sizeRepository.existsById(uuid);
    }

    @Override
    public long count() {
        return sizeRepository.count();
    }

    @Override
    public void deleteById(UUID uuid) {
        sizeRepository.deleteById(uuid);
    }

    @Override
    public void delete(Size entity) {
        sizeRepository.delete(entity);
    }

    @Override
    public Size findByCode(String code) {
        return sizeRepository.findByCode(code).orElse(null);
    }
}
