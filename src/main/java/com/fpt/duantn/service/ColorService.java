package com.fpt.duantn.service;

import com.fpt.duantn.domain.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ColorService {
    Page<Color> findByType(Integer type, Pageable pageable);

    Page<Color> searchByKeyAndType(String key, Integer type, Pageable pageable);

    <S extends Color> List<S> saveAll(Iterable<S> entities);

    List<Color> findAll();

    List<Color> findAllById(Iterable<UUID> uuids);

    <S extends Color> S save(S entity);

    Optional<Color> findById(UUID uuid);

    boolean existsById(UUID uuid);

    long count();

    void deleteById(UUID uuid);

    void delete(Color entity);

    Color findByCode(String code);
}
