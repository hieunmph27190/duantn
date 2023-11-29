package com.fpt.duantn.repository;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Role;
import com.fpt.duantn.models.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
//    insert roles(id,code,name,type) values('25BD71E9-A7E5-4F40-BCCD-519E17396FD8','ADMIN',N'Quản Lí',1),
//            ('C0540337-100A-4593-9970-64CE29D4A353','NV',N'Nhân Viên',1)

    public Role findByCode(String code);

    public Optional<Role> findByName(ERole name);

}
