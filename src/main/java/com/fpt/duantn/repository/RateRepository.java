
package com.fpt.duantn.repository;

import com.fpt.duantn.domain.BillDetail;

import com.fpt.duantn.domain.ProductDetail;
import com.fpt.duantn.domain.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {

	List<Rate> findAllByOrderByIdDesc();

	Rate findByBillDetail(BillDetail billDetail);

	List<Rate> findByProductOrderByIdDesc(ProductDetail productdetail);

}
