package com.fpt.duantn.service;

import com.fpt.duantn.domain.Bill;
import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.dto.BillReponse;
import com.fpt.duantn.dto.BillSellOnReponse;
import com.fpt.duantn.dto.CustomerReponse;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public interface BillService {

    Page<BillReponse> searchByKeyword(String key, Integer type, Pageable pageable);

    Page<BillReponse> searchByKeyword(String key, String phoneNumber, Timestamp startTime, Timestamp endTime, Integer paymentType, Integer type, UUID employeeID, Pageable pageable);

    Page<BillSellOnReponse> searchByKeyword(UUID customerId, Integer type, Pageable pageable);

    List<Bill> findByCustomer(Customer customer);

    <S extends Bill> S saveAndFlush(S entity);

    <S extends Bill> List<S> saveAllAndFlush(Iterable<S> entities);

    @Deprecated
    void deleteInBatch(Iterable<Bill> entities);

    void deleteAllInBatch(Iterable<Bill> entities);

    void deleteAllByIdInBatch(Iterable<UUID> uuids);

    void deleteAllInBatch();

    @Deprecated
    Bill getOne(UUID uuid);

    @Deprecated
    Bill getById(UUID uuid);

    Bill getReferenceById(UUID uuid);

    <S extends Bill> List<S> findAll(Example<S> example);

    <S extends Bill> List<S> findAll(Example<S> example, Sort sort);

    <S extends Bill> List<S> saveAll(Iterable<S> entities);

    List<Bill> findAll();

    List<Bill> findAllById(Iterable<UUID> uuids);

    <S extends Bill> S save(S entity);

    Optional<Bill> findById(UUID uuid);

    boolean existsById(UUID uuid);

    long count();

    void deleteById(UUID uuid);

    void delete(Bill entity);

    void deleteAllById(Iterable<? extends UUID> uuids);

    void deleteAll(Iterable<? extends Bill> entities);

    void deleteAll();

    List<Bill> findAll(Sort sort);

    Page<Bill> findAll(Pageable pageable);

    <S extends Bill> Optional<S> findOne(Example<S> example);

    <S extends Bill> Page<S> findAll(Example<S> example, Pageable pageable);

    <S extends Bill> long count(Example<S> example);

    <S extends Bill> boolean exists(Example<S> example);

    <S extends Bill, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction);
}
