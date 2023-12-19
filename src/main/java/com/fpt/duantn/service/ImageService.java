package com.fpt.duantn.service;

import com.fpt.duantn.domain.Image;
import com.fpt.duantn.domain.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ImageService {

    Page<Image> findByType(Integer type, Pageable pageable);

    List<Image> findByProductId(UUID id, Integer type);

    List<Image> findByProductIdAndProductType(UUID id, Integer type);

    void deleteAllById(Iterable<? extends UUID> uuids);

    List<UUID> findIDByProductId(UUID id, Integer type);

    Page<Image> findByProductIdAndProductType(UUID id, Integer type, Pageable pageable);

    <S extends Image> List<S> saveAll(Iterable<S> entities);

    List<Image> findAll();

    List<Image> findAllById(Iterable<UUID> uuids);

    <S extends Image> S save(S entity);

    Optional<Image> findById(UUID uuid);

    boolean existsById(UUID uuid);

    long count();

    void deleteById(UUID uuid);

    void delete(Image entity);

    void deleteAll(Iterable<? extends Image> entities);
}
