package com.fpt.duantn.repository;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.domain.Employee;
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
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    @Query("SELECT new com.fpt.duantn.dto.EmployeeReponse(e.id,e.name,e.gender,e.dateOfBirth,e.address,e.email,e.phoneNumber,CASE WHEN e.image IS NOT NULL THEN TRUE ELSE FALSE END,e.type) from  Employee e where  ( CAST(e.id AS string) like :key " +
            "or e.userName like concat('%',:key,'%') " +
            "or e.name like concat('%',:key,'%') " +
            "or e.phoneNumber like concat('%',:key,'%') " +
            "or e.address like concat('%',:key,'%') " +
            "or e.email like concat('%',:key,'%')) " +
            "and (:type is null or e.type = :type) " +
            "and e.role.code='NV'")
    public Page<EmployeeReponse> searchByKeyword(String key , Integer type, Pageable pageable);


    @Query("SELECT e.image from Employee e where e.id = :id")
    public Optional<Blob> findImageById(UUID id);

    @Query("SELECT e from  Employee e where e.phoneNumber like :phoneNumber" )
    public Optional<Employee> findEByPhoneNumber(String phoneNumber);


    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET " +
            "e.name = :#{#updatedEmployee.name}, " +
            "e.userName = :#{#updatedEmployee.userName}, " +
            "e.password = :#{#updatedEmployee.password}, " +
            "e.type = :#{#updatedEmployee.type}, " +
            "e.address = :#{#updatedEmployee.address}, " +
            "e.dateOfBirth = :#{#updatedEmployee.dateOfBirth}, " +
            "e.email = :#{#updatedEmployee.email}, " +
            "e.gender = :#{#updatedEmployee.gender}, " +
            "e.phoneNumber = :#{#updatedEmployee.phoneNumber}, " +
            "e.role = :#{#updatedEmployee.role} " +
            "WHERE e.id = :#{#updatedEmployee.id}")
    public Integer updateEmployeeWithoutImage(@Param("updatedEmployee") Employee updatedEmployee);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET " +
            "e.image = :image " +
            "WHERE e.id = :id")
    public Integer updateEmployeeImage(@Param("id") UUID id,@Param("image") Blob image);

    public Boolean existsByEmail(String email);
    public Optional<Employee> findByEmail(String email);

}
