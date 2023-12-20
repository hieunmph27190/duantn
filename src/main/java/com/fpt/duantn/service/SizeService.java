package com.fpt.duantn.service;


import com.fpt.duantn.domain.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface SizeService {
    Page<Size> findByType(Integer type, Pageable pageable);

    Page<Size> searchByKeyAndType(String key, Integer type, Pageable pageable);

    <S extends Size> List<S> saveAll(Iterable<S> entities);

    List<Size> findAll();

    List<Size> findAllById(Iterable<UUID> uuids);

    <S extends Size> S save(S entity);

    Optional<Size> findById(UUID uuid);

    boolean existsById(UUID uuid);

    long count();

    void deleteById(UUID uuid);

    void delete(Size entity);

    Size findByCode(String code);
}
