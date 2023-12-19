package com.fpt.duantn.service;

import com.fpt.duantn.domain.Employee;
import com.fpt.duantn.dto.EmployeeReponse;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public interface EmployeeService {
    Optional<Employee> findEByPhoneNumber(String phoneNumber);

    Integer updateEmployeeWithoutImage(Employee updatedEmployee);

    Integer updateEmployeeImage(UUID id, Blob image);

    void flush();

    Page<EmployeeReponse> searchByKeyword(String key, Integer type, Pageable pageable);

    Optional<Blob> findImageById(UUID id);

    <S extends Employee> S saveAndFlush(S entity);

    <S extends Employee> List<S> saveAllAndFlush(Iterable<S> entities);

    @Deprecated
    void deleteInBatch(Iterable<Employee> entities);

    void deleteAllInBatch(Iterable<Employee> entities);

    void deleteAllByIdInBatch(Iterable<UUID> uuids);

    void deleteAllInBatch();

    @Deprecated
    Employee getOne(UUID uuid);

    @Deprecated
    Employee getById(UUID uuid);

    Employee getReferenceById(UUID uuid);

    <S extends Employee> List<S> findAll(Example<S> example);

    <S extends Employee> List<S> findAll(Example<S> example, Sort sort);

    <S extends Employee> List<S> saveAll(Iterable<S> entities);

    List<Employee> findAll();

    List<Employee> findAllById(Iterable<UUID> uuids);

    <S extends Employee> S save(S entity);

    Optional<Employee> findById(UUID uuid);

    boolean existsById(UUID uuid);

    long count();

    void deleteById(UUID uuid);

    void delete(Employee entity);

    void deleteAllById(Iterable<? extends UUID> uuids);

    void deleteAll(Iterable<? extends Employee> entities);

    void deleteAll();

    List<Employee> findAll(Sort sort);

    Page<Employee> findAll(Pageable pageable);

    <S extends Employee> Optional<S> findOne(Example<S> example);

    <S extends Employee> Page<S> findAll(Example<S> example, Pageable pageable);

    <S extends Employee> long count(Example<S> example);

    Boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);

    <S extends Employee> boolean exists(Example<S> example);

    <S extends Employee, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction);
}
