package com.fpt.duantn.service.impl;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Image;
import com.fpt.duantn.repository.BrandRepository;
import com.fpt.duantn.repository.ImageRepository;
import com.fpt.duantn.service.BrandService;
import com.fpt.duantn.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Page<Image> findByType(Integer type, Pageable pageable) {
        return imageRepository.findByType(type, pageable);
    }


    @Override
    public List<Image> findByProductId(UUID id, Integer type) {
        return imageRepository.findByProductId(id, type);
    }

    @Override
    public List<Image> findByProductIdAndProductType(UUID id, Integer type) {
        return imageRepository.findByProductIdAndProductType(id, type);
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        imageRepository.deleteAllById(uuids);
    }

    @Override
    public List<UUID> findIDByProductId(UUID id, Integer type) {
        return imageRepository.findIDByProductId(id, type);
    }

    @Override
    public Page<Image> findByProductIdAndProductType(UUID id, Integer type, Pageable pageable) {
        return imageRepository.findByProductIdAndProductType(id, type, pageable);
    }

    @Override
    public <S extends Image> List<S> saveAll(Iterable<S> entities) {
        return imageRepository.saveAll(entities);
    }

    @Override
    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    @Override
    public List<Image> findAllById(Iterable<UUID> uuids) {
        return imageRepository.findAllById(uuids);
    }

    @Override
    public <S extends Image> S save(S entity) {
        return imageRepository.save(entity);
    }

    @Override
    public Optional<Image> findById(UUID uuid) {
        return imageRepository.findById(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return imageRepository.existsById(uuid);
    }

    @Override
    public long count() {
        return imageRepository.count();
    }

    @Override
    public void deleteById(UUID uuid) {
        imageRepository.deleteById(uuid);
    }

    @Override
    public void delete(Image entity) {
        imageRepository.delete(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends Image> entities) {
        imageRepository.deleteAll(entities);
    }
}
