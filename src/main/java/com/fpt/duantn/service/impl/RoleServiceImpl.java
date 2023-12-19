package com.fpt.duantn.service.impl;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Role;
import com.fpt.duantn.models.ERole;
import com.fpt.duantn.repository.BrandRepository;
import com.fpt.duantn.repository.RoleRepository;
import com.fpt.duantn.service.BrandService;
import com.fpt.duantn.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(ERole name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role findByCode(String code) {
        return roleRepository.findByCode(code);
    }

    @Override
    public void flush() {
        roleRepository.flush();
    }

    @Override
    public <S extends Role> S saveAndFlush(S entity) {
        return roleRepository.saveAndFlush(entity);
    }

    @Override
    public <S extends Role> List<S> saveAllAndFlush(Iterable<S> entities) {
        return roleRepository.saveAllAndFlush(entities);
    }

    @Override
    @Deprecated
    public void deleteInBatch(Iterable<Role> entities) {
        roleRepository.deleteInBatch(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<Role> entities) {
        roleRepository.deleteAllInBatch(entities);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {
        roleRepository.deleteAllByIdInBatch(uuids);
    }

    @Override
    public void deleteAllInBatch() {
        roleRepository.deleteAllInBatch();
    }

    @Override
    @Deprecated
    public Role getOne(UUID uuid) {
        return roleRepository.getOne(uuid);
    }

    @Override
    @Deprecated
    public Role getById(UUID uuid) {
        return roleRepository.getById(uuid);
    }

    @Override
    public Role getReferenceById(UUID uuid) {
        return roleRepository.getReferenceById(uuid);
    }

    @Override
    public <S extends Role> List<S> findAll(Example<S> example) {
        return roleRepository.findAll(example);
    }

    @Override
    public <S extends Role> List<S> findAll(Example<S> example, Sort sort) {
        return roleRepository.findAll(example, sort);
    }

    @Override
    public <S extends Role> List<S> saveAll(Iterable<S> entities) {
        return roleRepository.saveAll(entities);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> findAllById(Iterable<UUID> uuids) {
        return roleRepository.findAllById(uuids);
    }

    @Override
    public <S extends Role> S save(S entity) {
        return roleRepository.save(entity);
    }

    @Override
    public Optional<Role> findById(UUID uuid) {
        return roleRepository.findById(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return roleRepository.existsById(uuid);
    }

    @Override
    public long count() {
        return roleRepository.count();
    }

    @Override
    public void deleteById(UUID uuid) {
        roleRepository.deleteById(uuid);
    }

    @Override
    public void delete(Role entity) {
        roleRepository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        roleRepository.deleteAllById(uuids);
    }

    @Override
    public void deleteAll(Iterable<? extends Role> entities) {
        roleRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        roleRepository.deleteAll();
    }

    @Override
    public List<Role> findAll(Sort sort) {
        return roleRepository.findAll(sort);
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public <S extends Role> Optional<S> findOne(Example<S> example) {
        return roleRepository.findOne(example);
    }

    @Override
    public <S extends Role> Page<S> findAll(Example<S> example, Pageable pageable) {
        return roleRepository.findAll(example, pageable);
    }

    @Override
    public <S extends Role> long count(Example<S> example) {
        return roleRepository.count(example);
    }

    @Override
    public <S extends Role> boolean exists(Example<S> example) {
        return roleRepository.exists(example);
    }

    @Override
    public <S extends Role, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return roleRepository.findBy(example, queryFunction);
    }
}
