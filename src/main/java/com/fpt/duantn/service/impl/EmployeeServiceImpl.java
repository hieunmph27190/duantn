package com.fpt.duantn.service.impl;

import com.fpt.duantn.domain.Employee;
import com.fpt.duantn.dto.EmployeeReponse;
import com.fpt.duantn.repository.EmployeeRepository;
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

import java.sql.Blob;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    public Optional<Employee> findEByPhoneNumber(String phoneNumber) {
        return employeeRepository.findEByPhoneNumber(phoneNumber);
    }

    @Override
    public Integer updateEmployeeWithoutImage(Employee updatedEmployee) {
        return employeeRepository.updateEmployeeWithoutImage(updatedEmployee);
    }

    @Override
    public Integer updateEmployeeImage(UUID id, Blob image) {
        return employeeRepository.updateEmployeeImage(id, image);
    }

    @Override
    public void flush() {
        employeeRepository.flush();
    }
    @Override
    public Page<EmployeeReponse> searchByKeyword(String key, Integer type, Pageable pageable) {
        return employeeRepository.searchByKeyword(key, type, pageable);
    }

    @Override
    public Optional<Blob> findImageById(UUID id) {
        return employeeRepository.findImageById(id);
    }

    @Override
    public <S extends Employee> S saveAndFlush(S entity) {
        return employeeRepository.saveAndFlush(entity);
    }

    @Override
    public <S extends Employee> List<S> saveAllAndFlush(Iterable<S> entities) {
        return employeeRepository.saveAllAndFlush(entities);
    }

    @Override
    @Deprecated
    public void deleteInBatch(Iterable<Employee> entities) {
        employeeRepository.deleteInBatch(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<Employee> entities) {
        employeeRepository.deleteAllInBatch(entities);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {
        employeeRepository.deleteAllByIdInBatch(uuids);
    }

    @Override
    public void deleteAllInBatch() {
        employeeRepository.deleteAllInBatch();
    }

    @Override
    @Deprecated
    public Employee getOne(UUID uuid) {
        return employeeRepository.getOne(uuid);
    }

    @Override
    @Deprecated
    public Employee getById(UUID uuid) {
        return employeeRepository.getById(uuid);
    }

    @Override
    public Employee getReferenceById(UUID uuid) {
        return employeeRepository.getReferenceById(uuid);
    }

    @Override
    public <S extends Employee> List<S> findAll(Example<S> example) {
        return employeeRepository.findAll(example);
    }

    @Override
    public <S extends Employee> List<S> findAll(Example<S> example, Sort sort) {
        return employeeRepository.findAll(example, sort);
    }

    @Override
    public <S extends Employee> List<S> saveAll(Iterable<S> entities) {
        return employeeRepository.saveAll(entities);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> findAllById(Iterable<UUID> uuids) {
        return employeeRepository.findAllById(uuids);
    }

    @Override
    public <S extends Employee> S save(S entity) {
        return employeeRepository.save(entity);
    }

    @Override
    public Optional<Employee> findById(UUID uuid) {
        return employeeRepository.findById(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return employeeRepository.existsById(uuid);
    }

    @Override
    public long count() {
        return employeeRepository.count();
    }

    @Override
    public void deleteById(UUID uuid) {
        employeeRepository.deleteById(uuid);
    }

    @Override
    public void delete(Employee entity) {
        employeeRepository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        employeeRepository.deleteAllById(uuids);
    }

    @Override
    public void deleteAll(Iterable<? extends Employee> entities) {
        employeeRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        employeeRepository.deleteAll();
    }

    @Override
    public List<Employee> findAll(Sort sort) {
        return employeeRepository.findAll(sort);
    }

    @Override
    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public <S extends Employee> Optional<S> findOne(Example<S> example) {
        return employeeRepository.findOne(example);
    }

    @Override
    public <S extends Employee> Page<S> findAll(Example<S> example, Pageable pageable) {
        return employeeRepository.findAll(example, pageable);
    }

    @Override
    public <S extends Employee> long count(Example<S> example) {
        return employeeRepository.count(example);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public <S extends Employee> boolean exists(Example<S> example) {
        return employeeRepository.exists(example);
    }

    @Override
    public <S extends Employee, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return employeeRepository.findBy(example, queryFunction);
    }
}
