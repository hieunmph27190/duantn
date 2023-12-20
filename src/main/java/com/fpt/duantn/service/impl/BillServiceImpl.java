package com.fpt.duantn.service.impl;

import com.fpt.duantn.domain.Bill;
import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.dto.BillReponse;
import com.fpt.duantn.dto.BillSellOnReponse;
import com.fpt.duantn.dto.CustomerReponse;
import com.fpt.duantn.repository.BillRepository;
import com.fpt.duantn.repository.CustomerRepository;
import com.fpt.duantn.service.BillService;
import com.fpt.duantn.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillRepository billRepository;

    @Override
    public Page<BillReponse> searchByKeyword(String key, Integer type, Pageable pageable) {
        return billRepository.searchByKeyword(key, type, pageable);
    }

    public List<Bill> findByPaymentTypeAndTypeAndBillCreateDateBefore(Integer paymentType, Integer type, LocalDateTime billCreateDate) {
        return billRepository.findByPaymentTypeAndTypeAndBillCreateDateBefore(paymentType, type, billCreateDate);
    }

    @Override
    public Page<BillReponse> searchByKeyword(String key, String phoneNumber, Timestamp startTime, Timestamp endTime, Integer paymentType, Integer type, UUID employeeID, Pageable pageable) {
        return billRepository.searchByKeyword(key, phoneNumber, startTime, endTime, paymentType, type, employeeID, pageable);
    }

    @Override
    public Page<BillSellOnReponse> searchByKeyword(UUID customerId, Integer type, Pageable pageable) {
        return billRepository.searchByKeyword(customerId, type, pageable);
    }

    @Override
    public List<Bill> findByCustomer(Customer customer) {
        return billRepository.findByCustomer(customer);
    }

    public void flush() {
        billRepository.flush();
    }

    @Override
    public <S extends Bill> S saveAndFlush(S entity) {
        return billRepository.saveAndFlush(entity);
    }

    @Override
    public <S extends Bill> List<S> saveAllAndFlush(Iterable<S> entities) {
        return billRepository.saveAllAndFlush(entities);
    }

    @Override
    @Deprecated
    public void deleteInBatch(Iterable<Bill> entities) {
        billRepository.deleteInBatch(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<Bill> entities) {
        billRepository.deleteAllInBatch(entities);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {
        billRepository.deleteAllByIdInBatch(uuids);
    }

    @Override
    public void deleteAllInBatch() {
        billRepository.deleteAllInBatch();
    }

    @Override
    @Deprecated
    public Bill getOne(UUID uuid) {
        return billRepository.getOne(uuid);
    }

    @Override
    @Deprecated
    public Bill getById(UUID uuid) {
        return billRepository.getById(uuid);
    }

    @Override
    public Bill getReferenceById(UUID uuid) {
        return billRepository.getReferenceById(uuid);
    }

    @Override
    public <S extends Bill> List<S> findAll(Example<S> example) {
        return billRepository.findAll(example);
    }

    @Override
    public <S extends Bill> List<S> findAll(Example<S> example, Sort sort) {
        return billRepository.findAll(example, sort);
    }

    @Override
    public <S extends Bill> List<S> saveAll(Iterable<S> entities) {
        return billRepository.saveAll(entities);
    }

    @Override
    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    @Override
    public List<Bill> findAllById(Iterable<UUID> uuids) {
        return billRepository.findAllById(uuids);
    }

    @Override
    public <S extends Bill> S save(S entity) {
        return billRepository.save(entity);
    }

    @Override
    public Optional<Bill> findById(UUID uuid) {
        return billRepository.findById(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return billRepository.existsById(uuid);
    }

    @Override
    public long count() {
        return billRepository.count();
    }

    @Override
    public void deleteById(UUID uuid) {
        billRepository.deleteById(uuid);
    }

    @Override
    public void delete(Bill entity) {
        billRepository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        billRepository.deleteAllById(uuids);
    }

    @Override
    public void deleteAll(Iterable<? extends Bill> entities) {
        billRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        billRepository.deleteAll();
    }

    @Override
    public List<Bill> findAll(Sort sort) {
        return billRepository.findAll(sort);
    }

    @Override
    public Page<Bill> findAll(Pageable pageable) {
        return billRepository.findAll(pageable);
    }

    @Override
    public <S extends Bill> Optional<S> findOne(Example<S> example) {
        return billRepository.findOne(example);
    }

    @Override
    public <S extends Bill> Page<S> findAll(Example<S> example, Pageable pageable) {
        return billRepository.findAll(example, pageable);
    }

    @Override
    public <S extends Bill> long count(Example<S> example) {
        return billRepository.count(example);
    }

    @Override
    public <S extends Bill> boolean exists(Example<S> example) {
        return billRepository.exists(example);
    }

    @Override
    public <S extends Bill, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return billRepository.findBy(example, queryFunction);
    }
}
