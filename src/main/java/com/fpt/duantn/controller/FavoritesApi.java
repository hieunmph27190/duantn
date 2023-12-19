
package com.fpt.duantn.controller;

import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.domain.Favorite;
import com.fpt.duantn.domain.ProductDetail;
import com.fpt.duantn.repository.FavoriteRepository;
import com.fpt.duantn.repository.ProductDetailRepository;
import com.fpt.duantn.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/favorites")
public class FavoritesApi {


	@Autowired
	FavoriteRepository favoriteRepository;

	@Autowired
	CustomerService customerService;

	@Autowired
	ProductDetailRepository productDetailRepository;


	@GetMapping("email/{email}")
	public ResponseEntity<List<Favorite>> findByEmail(@PathVariable("email") String email) {
		if (customerService.existsByEmail(email)) {
			return ResponseEntity.ok(favoriteRepository.findByCustomer(customerService.findByEmail(email).get()));
		}
		return ResponseEntity.notFound().build();
	}


//	@GetMapping("productdetail/{id}")
//	public ResponseEntity<Integer> findByProduct(@PathVariable("id") UUID id) {
//		if (productDetailRepository.existsById(id)) {
//			return ResponseEntity.ok(favoriteRepository.countByProductdetail(productDetailRepository.getById(id)));
//		}
//		System.out.println(id);
//		return ResponseEntity.notFound().build();
//	}


//	@GetMapping("{id}/{email}")
//	public ResponseEntity<Favorite> findByProductAndUser(@PathVariable("id") UUID id,
//														 @PathVariable("email") String email) {
//		if (customerService.existsByEmail(email)) {
//			if (productDetailRepository.existsById(id)) {
//				ProductDetail productDetail = productDetailRepository.findById(id).get();
//				Customer user = customerService.findByEmail(email).get();
//				return ResponseEntity.ok(favoriteRepository.findByProductdetailAndCustomer(productDetail, user));
//			}
//
//		}
//
//		System.out.println(id);
//		System.out.println(email);
//
//		return ResponseEntity.notFound().build();
//	}



	@PostMapping("email")
	public ResponseEntity<Favorite> post(@RequestBody Favorite favorite) {
		return ResponseEntity.ok(favoriteRepository.save(favorite));
	}



	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		if (favoriteRepository.existsById(id)) {
			favoriteRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}






}
