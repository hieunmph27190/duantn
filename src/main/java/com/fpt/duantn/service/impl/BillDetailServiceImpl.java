package com.fpt.duantn.service.impl;

import com.fpt.duantn.domain.Bill;
import com.fpt.duantn.domain.BillDetail;
import com.fpt.duantn.dto.BillDetailReponse;
import com.fpt.duantn.dto.BillReponse;
import com.fpt.duantn.dto.CustomerReponse;
import com.fpt.duantn.repository.BillDetailRepository;
import com.fpt.duantn.repository.BillRepository;
import com.fpt.duantn.service.BillDetailService;
import com.fpt.duantn.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public class BillDetailServiceImpl implements BillDetailService {
    @Autowired
    private BillDetailRepository billDetailRepository;

    @Override
    public Page<BillDetailReponse> searchByKeyword(String key, Integer type, UUID billId, Pageable pageable) {
        return billDetailRepository.searchByKeyword(key, type, billId, pageable);
    }

    @Override
    public List<BillDetail> findByBillIdAndType(UUID billId, Integer type) {
        return billDetailRepository.findByBillIdAndType(billId, type);
    }

    @Override
    public List<BillDetail> findByBillId(UUID id) {
        return billDetailRepository.findByBillId(id);
    }


    @Override
    public Optional<Long> sumQuantityByBillIdAndType(UUID billId, Integer type) {
        return billDetailRepository.sumQuantityByBillIdAndType(billId, type);
    }

    @Override
    public Optional<Double> sumMoneyByBillIdAndType(UUID id, Integer type) {
        return billDetailRepository.sumMoneyByBillIdAndType(id, type);
    }

    @Override
    public <S extends BillDetail> S saveAndFlush(S entity) {
        return billDetailRepository.saveAndFlush(entity);
    }

    @Override
    public <S extends BillDetail> List<S> saveAllAndFlush(Iterable<S> entities) {
        return billDetailRepository.saveAllAndFlush(entities);
    }

    @Override
    @Deprecated
    public void deleteInBatch(Iterable<BillDetail> entities) {
        billDetailRepository.deleteInBatch(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<BillDetail> entities) {
        billDetailRepository.deleteAllInBatch(entities);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {
        billDetailRepository.deleteAllByIdInBatch(uuids);
    }

    @Override
    public void deleteAllInBatch() {
        billDetailRepository.deleteAllInBatch();
    }

    @Override
    @Deprecated
    public BillDetail getOne(UUID uuid) {
        return billDetailRepository.getOne(uuid);
    }

    @Override
    @Deprecated
    public BillDetail getById(UUID uuid) {
        return billDetailRepository.getById(uuid);
    }

    @Override
    public BillDetail getReferenceById(UUID uuid) {
        return billDetailRepository.getReferenceById(uuid);
    }

    @Override
    public <S extends BillDetail> List<S> findAll(Example<S> example) {
        return billDetailRepository.findAll(example);
    }

    @Override
    public <S extends BillDetail> List<S> findAll(Example<S> example, Sort sort) {
        return billDetailRepository.findAll(example, sort);
    }

    @Override
    public <S extends BillDetail> List<S> saveAll(Iterable<S> entities) {
        return billDetailRepository.saveAll(entities);
    }

    @Override
    public List<BillDetail> findAll() {
        return billDetailRepository.findAll();
    }

    @Override
    public List<BillDetail> findAllById(Iterable<UUID> uuids) {
        return billDetailRepository.findAllById(uuids);
    }

    @Override
    public <S extends BillDetail> S save(S entity) {
        return billDetailRepository.save(entity);
    }

    @Override
    public Optional<BillDetail> findById(UUID uuid) {
        return billDetailRepository.findById(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return billDetailRepository.existsById(uuid);
    }

    @Override
    public long count() {
        return billDetailRepository.count();
    }

    @Override
    public void deleteById(UUID uuid) {
        billDetailRepository.deleteById(uuid);
    }

    @Override
    public void delete(BillDetail entity) {
        billDetailRepository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        billDetailRepository.deleteAllById(uuids);
    }

    @Override
    public void deleteAll(Iterable<? extends BillDetail> entities) {
        billDetailRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        billDetailRepository.deleteAll();
    }

    @Override
    public List<BillDetail> findAll(Sort sort) {
        return billDetailRepository.findAll(sort);
    }

    @Override
    public Page<BillDetail> findAll(Pageable pageable) {
        return billDetailRepository.findAll(pageable);
    }

    @Override
    public <S extends BillDetail> Optional<S> findOne(Example<S> example) {
        return billDetailRepository.findOne(example);
    }

    @Override
    public <S extends BillDetail> Page<S> findAll(Example<S> example, Pageable pageable) {
        return billDetailRepository.findAll(example, pageable);
    }

    @Override
    public <S extends BillDetail> long count(Example<S> example) {
        return billDetailRepository.count(example);
    }

    @Override
    public <S extends BillDetail> boolean exists(Example<S> example) {
        return billDetailRepository.exists(example);
    }

    @Override
    public <S extends BillDetail, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return billDetailRepository.findBy(example, queryFunction);
    }
}
