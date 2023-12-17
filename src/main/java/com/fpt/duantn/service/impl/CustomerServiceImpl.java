package com.fpt.duantn.service.impl;

import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.domain.Employee;
import com.fpt.duantn.dto.CustomerReponse;
import com.fpt.duantn.dto.EmployeeReponse;
import com.fpt.duantn.repository.CustomerRepository;
import com.fpt.duantn.repository.EmployeeRepository;
import com.fpt.duantn.service.CustomerService;
import com.fpt.duantn.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public Optional<Customer> findCByPhoneNumber(String phoneNumber) {
        return customerRepository.findCByPhoneNumber(phoneNumber);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Page<CustomerReponse> searchByKeyword(String key, Integer type, Pageable pageable) {
        return customerRepository.searchByKeyword(key, type, pageable);
    }

    @Override
    public CustomerReponse findByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<Blob> findImageById(UUID id) {
        return customerRepository.findImageById(id);
    }


    @Override
    public Integer updateCustomerWithoutImage(Customer customer) {
        return customerRepository.updateCustomerWithoutImage(customer);
    }

    @Override
    public Integer updateCustomerImage(UUID id, Blob image) {
        return customerRepository.updateCustomerImage(id, image);
    }

    @Override
    public void flush() {
        customerRepository.flush();
    }

    @Override
    public <S extends Customer> S saveAndFlush(S entity) {
        return customerRepository.saveAndFlush(entity);
    }

    @Override
    public <S extends Customer> List<S> saveAllAndFlush(Iterable<S> entities) {
        return customerRepository.saveAllAndFlush(entities);
    }

    @Override
    @Deprecated
    public void deleteInBatch(Iterable<Customer> entities) {
        customerRepository.deleteInBatch(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<Customer> entities) {
        customerRepository.deleteAllInBatch(entities);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {
        customerRepository.deleteAllByIdInBatch(uuids);
    }

    @Override
    public void deleteAllInBatch() {
        customerRepository.deleteAllInBatch();
    }

    @Override
    @Deprecated
    public Customer getOne(UUID uuid) {
        return customerRepository.getOne(uuid);
    }

    @Override
    @Deprecated
    public Customer getById(UUID uuid) {
        return customerRepository.getById(uuid);
    }

    @Override
    public Customer getReferenceById(UUID uuid) {
        return customerRepository.getReferenceById(uuid);
    }

    @Override
    public <S extends Customer> List<S> findAll(Example<S> example) {
        return customerRepository.findAll(example);
    }

    @Override
    public <S extends Customer> List<S> findAll(Example<S> example, Sort sort) {
        return customerRepository.findAll(example, sort);
    }

    @Override
    public <S extends Customer> List<S> saveAll(Iterable<S> entities) {
        return customerRepository.saveAll(entities);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> findAllById(Iterable<UUID> uuids) {
        return customerRepository.findAllById(uuids);
    }

    @Override
    public <S extends Customer> S save(S entity) {
        return customerRepository.save(entity);
    }

    @Override
    public Optional<Customer> findById(UUID uuid) {
        return customerRepository.findById(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return customerRepository.existsById(uuid);
    }

    @Override
    public long count() {
        return customerRepository.count();
    }

    @Override
    public void deleteById(UUID uuid) {
        customerRepository.deleteById(uuid);
    }

    @Override
    public void delete(Customer entity) {
        customerRepository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        customerRepository.deleteAllById(uuids);
    }

    @Override
    public void deleteAll(Iterable<? extends Customer> entities) {
        customerRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        customerRepository.deleteAll();
    }

    @Override
    public List<Customer> findAll(Sort sort) {
        return customerRepository.findAll(sort);
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public <S extends Customer> Optional<S> findOne(Example<S> example) {
        return customerRepository.findOne(example);
    }

    @Override
    public <S extends Customer> Page<S> findAll(Example<S> example, Pageable pageable) {
        return customerRepository.findAll(example, pageable);
    }

    @Override
    public <S extends Customer> long count(Example<S> example) {
        return customerRepository.count(example);
    }

    @Override
    public <S extends Customer> boolean exists(Example<S> example) {
        return customerRepository.exists(example);
    }

    @Override
    public <S extends Customer, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return customerRepository.findBy(example, queryFunction);
    }
}
