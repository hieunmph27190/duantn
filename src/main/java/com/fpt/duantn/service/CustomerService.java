package com.fpt.duantn.service;

import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.domain.Employee;
import com.fpt.duantn.dto.CustomerReponse;
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
public interface CustomerService {

    Optional<Customer> findCByPhoneNumber(String phoneNumber);

    Boolean existsByEmail(String email);

    Optional<Customer> findByEmail(String email);

    Page<CustomerReponse> searchByKeyword(String key, Integer type, Pageable pageable);

    CustomerReponse findByPhoneNumber(String phoneNumber);

    Optional<Blob> findImageById(UUID id);

    Integer updateCustomerWithoutImage(Customer customer);

    Integer updateCustomerImage(UUID id, Blob image);

    void flush();

    <S extends Customer> S saveAndFlush(S entity);

    <S extends Customer> List<S> saveAllAndFlush(Iterable<S> entities);

    @Deprecated
    void deleteInBatch(Iterable<Customer> entities);

    void deleteAllInBatch(Iterable<Customer> entities);

    void deleteAllByIdInBatch(Iterable<UUID> uuids);

    void deleteAllInBatch();

    @Deprecated
    Customer getOne(UUID uuid);

    @Deprecated
    Customer getById(UUID uuid);

    Customer getReferenceById(UUID uuid);

    <S extends Customer> List<S> findAll(Example<S> example);

    <S extends Customer> List<S> findAll(Example<S> example, Sort sort);

    <S extends Customer> List<S> saveAll(Iterable<S> entities);

    List<Customer> findAll();

    List<Customer> findAllById(Iterable<UUID> uuids);

    <S extends Customer> S save(S entity);

    Optional<Customer> findById(UUID uuid);

    boolean existsById(UUID uuid);

    long count();

    void deleteById(UUID uuid);

    void delete(Customer entity);

    void deleteAllById(Iterable<? extends UUID> uuids);

    void deleteAll(Iterable<? extends Customer> entities);

    void deleteAll();

    List<Customer> findAll(Sort sort);

    Page<Customer> findAll(Pageable pageable);

    <S extends Customer> Optional<S> findOne(Example<S> example);

    <S extends Customer> Page<S> findAll(Example<S> example, Pageable pageable);

    <S extends Customer> long count(Example<S> example);

    <S extends Customer> boolean exists(Example<S> example);

    <S extends Customer, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction);
}
