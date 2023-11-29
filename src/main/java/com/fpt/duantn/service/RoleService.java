package com.fpt.duantn.service;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Role;
import com.fpt.duantn.models.ERole;
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
public interface RoleService {

    Optional<Role> findByName(ERole name);

    Role findByCode(String code);

    void flush();

    <S extends Role> S saveAndFlush(S entity);

    <S extends Role> List<S> saveAllAndFlush(Iterable<S> entities);

    @Deprecated
    void deleteInBatch(Iterable<Role> entities);

    void deleteAllInBatch(Iterable<Role> entities);

    void deleteAllByIdInBatch(Iterable<UUID> uuids);

    void deleteAllInBatch();

    @Deprecated
    Role getOne(UUID uuid);

    @Deprecated
    Role getById(UUID uuid);

    Role getReferenceById(UUID uuid);

    <S extends Role> List<S> findAll(Example<S> example);

    <S extends Role> List<S> findAll(Example<S> example, Sort sort);

    <S extends Role> List<S> saveAll(Iterable<S> entities);

    List<Role> findAll();

    List<Role> findAllById(Iterable<UUID> uuids);

    <S extends Role> S save(S entity);

    Optional<Role> findById(UUID uuid);

    boolean existsById(UUID uuid);

    long count();

    void deleteById(UUID uuid);

    void delete(Role entity);

    void deleteAllById(Iterable<? extends UUID> uuids);

    void deleteAll(Iterable<? extends Role> entities);

    void deleteAll();

    List<Role> findAll(Sort sort);

    Page<Role> findAll(Pageable pageable);

    <S extends Role> Optional<S> findOne(Example<S> example);

    <S extends Role> Page<S> findAll(Example<S> example, Pageable pageable);

    <S extends Role> long count(Example<S> example);

    <S extends Role> boolean exists(Example<S> example);

    <S extends Role, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction);
}
