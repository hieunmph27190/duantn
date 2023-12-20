package com.fpt.duantn.service.impl;

import com.fpt.duantn.domain.Color;
import com.fpt.duantn.repository.ColorRepository;
import com.fpt.duantn.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ColorServiceImpl implements ColorService {
    @Autowired
    private ColorRepository colorRepository;

    @Override
    public Page<Color> findByType(Integer type, Pageable pageable) {
        return colorRepository.findByType(type, pageable);
    }


    @Override
    public Page<Color> searchByKeyAndType(String key, Integer type, Pageable pageable) {
        return colorRepository.searchByKeyAndType(key, type, pageable);
    }

    @Override
    public <S extends Color> List<S> saveAll(Iterable<S> entities) {
        return colorRepository.saveAll(entities);
    }

    @Override
    public List<Color> findAll() {
        return colorRepository.findAll();
    }

    @Override
    public List<Color> findAllById(Iterable<UUID> uuids) {
        return colorRepository.findAllById(uuids);
    }

    @Override
    public <S extends Color> S save(S entity) {
        return colorRepository.save(entity);
    }

    @Override
    public Optional<Color> findById(UUID uuid) {
        return colorRepository.findById(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return colorRepository.existsById(uuid);
    }

    @Override
    public long count() {
        return colorRepository.count();
    }

    @Override
    public void deleteById(UUID uuid) {
        colorRepository.deleteById(uuid);
    }

    @Override
    public void delete(Color entity) {
        colorRepository.delete(entity);
    }

    @Override
    public Color findByCode(String code) {
        return colorRepository.findByCode(code).orElse(null);
    }
}
