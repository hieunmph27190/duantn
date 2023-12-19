/*
 * (C) Copyright 2022. All Rights Reserved.
 *
 * @author DongTHD
 * @date Mar 10, 2022
*/
package com.fpt.duantn.repository;

import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.domain.Favorite;
import com.fpt.duantn.domain.Product;
import com.fpt.duantn.domain.ProductDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	List<Favorite> findByCustomer(Customer customer);
	Integer countByProduct(Product product);
	Favorite findByProductAndCustomer(Product product, Customer customer);


//	Integer countByProduct(Product product);
//	Favorite findByProductAndUser(Product product, User user);
}
