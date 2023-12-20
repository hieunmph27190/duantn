package com.fpt.duantn.service;

import com.fpt.duantn.domain.Color;
import com.fpt.duantn.domain.Sole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface SoleService {
    Page<Sole> findByType(Integer type, Pageable pageable);

    Page<Sole> searchByKeyAndType(String key, Integer type, Pageable pageable);

    <S extends Sole> List<S> saveAll(Iterable<S> entities);

    List<Sole> findAll();

    List<Sole> findAllById(Iterable<UUID> uuids);

    <S extends Sole> S save(S entity);

    Optional<Sole> findById(UUID uuid);

    boolean existsById(UUID uuid);

    long count();

    void deleteById(UUID uuid);

    void delete(Sole entity);

    Sole findByCode(String code);
}
