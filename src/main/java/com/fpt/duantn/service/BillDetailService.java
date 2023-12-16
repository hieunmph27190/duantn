package com.fpt.duantn.service;

import com.fpt.duantn.domain.Bill;
import com.fpt.duantn.domain.BillDetail;
import com.fpt.duantn.dto.BillDetailReponse;
import com.fpt.duantn.dto.BillReponse;
import com.fpt.duantn.dto.CustomerReponse;
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
public interface BillDetailService {

    Page<BillDetailReponse> searchByKeyword(String key, Integer type, UUID billId, Pageable pageable);

    List<BillDetail> findByBillIdAndType(UUID billId, Integer type);

    List<BillDetail> findByBillId(UUID id);

    Optional<Long> sumQuantityByBillIdAndType(UUID billId, Integer type);

    Optional<Double> sumMoneyByBillIdAndType(UUID id, Integer type);

    <S extends BillDetail> S saveAndFlush(S entity);

    <S extends BillDetail> List<S> saveAllAndFlush(Iterable<S> entities);

    @Deprecated
    void deleteInBatch(Iterable<BillDetail> entities);

    void deleteAllInBatch(Iterable<BillDetail> entities);

    void deleteAllByIdInBatch(Iterable<UUID> uuids);

    void deleteAllInBatch();

    @Deprecated
    BillDetail getOne(UUID uuid);

    @Deprecated
    BillDetail getById(UUID uuid);

    BillDetail getReferenceById(UUID uuid);

    <S extends BillDetail> List<S> findAll(Example<S> example);

    <S extends BillDetail> List<S> findAll(Example<S> example, Sort sort);

    <S extends BillDetail> List<S> saveAll(Iterable<S> entities);

    List<BillDetail> findAll();

    List<BillDetail> findAllById(Iterable<UUID> uuids);

    <S extends BillDetail> S save(S entity);

    Optional<BillDetail> findById(UUID uuid);

    boolean existsById(UUID uuid);

    long count();

    void deleteById(UUID uuid);

    void delete(BillDetail entity);

    void deleteAllById(Iterable<? extends UUID> uuids);

    void deleteAll(Iterable<? extends BillDetail> entities);

    void deleteAll();

    List<BillDetail> findAll(Sort sort);

    Page<BillDetail> findAll(Pageable pageable);

    <S extends BillDetail> Optional<S> findOne(Example<S> example);

    <S extends BillDetail> Page<S> findAll(Example<S> example, Pageable pageable);

    <S extends BillDetail> long count(Example<S> example);

    <S extends BillDetail> boolean exists(Example<S> example);

    <S extends BillDetail, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction);
}
