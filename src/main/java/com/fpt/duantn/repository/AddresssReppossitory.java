package com.fpt.duantn.repository;


import com.fpt.duantn.domain.Addresss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface AddresssReppossitory extends JpaRepository<Addresss, UUID> {

}
