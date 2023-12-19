package com.fpt.duantn.repository;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.domain.Employee;
import com.fpt.duantn.dto.CustomerReponse;
import com.fpt.duantn.dto.EmployeeReponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    @Query("SELECT new com.fpt.duantn.dto.CustomerReponse(e.id,e.name,e.gender,e.dateOfBirth,e.address,e.email,e.phoneNumber,CASE WHEN e.image IS NOT NULL THEN TRUE ELSE FALSE END,e.type,e.ward,e.city,e.district) from  Customer e where  ( CAST(e.id AS string) like :key " +
            "or e.name like concat('%',:key,'%') " +
            "or e.phoneNumber like concat('%',:key,'%') " +
            "or e.ward like concat('%',:key,'%') " +
            "or e.city like concat('%',:key,'%') " +
            "or e.district like concat('%',:key,'%') " +
            "or e.address like concat('%',:key,'%') " +
            "or e.email like concat('%',:key,'%')) " +
            "and (:type is null or e.type = :type)")
    public Page<CustomerReponse> searchByKeyword(String key , Integer type, Pageable pageable);

    @Query("SELECT new com.fpt.duantn.dto.CustomerReponse(e.id,e.name,e.gender,e.dateOfBirth,e.address,e.email,e.phoneNumber,CASE WHEN e.image IS NOT NULL THEN TRUE ELSE FALSE END,e.type,e.ward,e.city,e.district) from  Customer e where e.phoneNumber like :phoneNumber" )
    public CustomerReponse findByPhoneNumber(String phoneNumber);

    @Query("SELECT e from  Customer e where e.phoneNumber like :phoneNumber" )
    public Optional<Customer> findCByPhoneNumber(String phoneNumber);


    @Query("SELECT e.image from Customer e where e.id = :id")
    public Optional<Blob> findImageById(UUID id);


    @Modifying
    @Transactional
    @Query("UPDATE Customer e SET " +
            "e.name = :#{#updatedCustomer.name}, " +
            "e.type = :#{#updatedCustomer.type}, " +
            "e.address = :#{#updatedCustomer.address}, " +
            "e.ward = :#{#updatedCustomer.ward}, " +
            "e.city = :#{#updatedCustomer.city}, " +
            "e.district = :#{#updatedCustomer.district}, " +
            "e.dateOfBirth = :#{#updatedCustomer.dateOfBirth}, " +
            "e.email = :#{#updatedCustomer.email}, " +
            "e.gender = :#{#updatedCustomer.gender}, " +
            "e.phoneNumber = :#{#updatedCustomer.phoneNumber} " +
            "WHERE e.id = :#{#updatedCustomer.id}")
    public Integer updateCustomerWithoutImage(@Param("updatedCustomer") Customer customer);

    @Modifying
    @Transactional
    @Query("UPDATE Customer e SET " +
            "e.image = :image " +
            "WHERE e.id = :id")
    public Integer updateCustomerImage(@Param("id") UUID id,@Param("image") Blob image);


    public Boolean existsByEmail(String email);
    public Optional<Customer> findByEmail(String email);

}
