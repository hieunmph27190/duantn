package com.fpt.duantn.service.impl;

import com.fpt.duantn.domain.Color;
import com.fpt.duantn.domain.Sole;
import com.fpt.duantn.repository.ColorRepository;
import com.fpt.duantn.repository.SoleRepository;
import com.fpt.duantn.service.ColorService;
import com.fpt.duantn.service.SoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SoleServiceImpl implements SoleService {
    @Autowired
    private SoleRepository soleRepository ;

    @Override
    public Page<Sole> findByType(Integer type, Pageable pageable) {
        return soleRepository.findByType(type, pageable);
    }


    @Override
    public Page<Sole> searchByKeyAndType(String key, Integer type, Pageable pageable) {
        return soleRepository.searchByKeyAndType(key, type, pageable);
    }

    @Override
    public <S extends Sole> List<S> saveAll(Iterable<S> entities) {
        return soleRepository.saveAll(entities);
    }

    @Override
    public List<Sole> findAll() {
        return soleRepository.findAll();
    }

    @Override
    public List<Sole> findAllById(Iterable<UUID> uuids) {
        return soleRepository.findAllById(uuids);
    }

    @Override
    public <S extends Sole> S save(S entity) {
        return soleRepository.save(entity);
    }

    @Override
    public Optional<Sole> findById(UUID uuid) {
        return soleRepository.findById(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return soleRepository.existsById(uuid);
    }

    @Override
    public long count() {
        return soleRepository.count();
    }

    @Override
    public void deleteById(UUID uuid) {
        soleRepository.deleteById(uuid);
    }

    @Override
    public void delete(Sole entity) {
        soleRepository.delete(entity);
    }


    @Override
    public Sole findByCode(String code) {
        return soleRepository.findByCode(code).orElse(null);
    }
}
