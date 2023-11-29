
package com.fpt.duantn.controller;

import com.fpt.duantn.domain.Cart;
import com.fpt.duantn.repository.CartRepository;
import com.fpt.duantn.repository.CartdetailRepository;
import com.fpt.duantn.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("api/cart")
public class CartApi {

	@Autowired
	CartRepository cartRepository;

	@Autowired
	CartdetailRepository cartDetailRepository;

	@Autowired
	CustomerService customerService;

//	@GetMapping("/user/{email}")
//	public ResponseEntity<Cart> getCartUser(@PathVariable("email") String email) {
//		if (!userRepository.existsByEmail(email)) {
//			return ResponseEntity.notFound().build();
//		}
//		return ResponseEntity.ok(cartRepository.findByUser(userRepository.findByEmail(email).get()));
//	}
//
//
//	@PutMapping("/user/{email}")
//	public ResponseEntity<Cart> putCartUser(@PathVariable("email") String email, @RequestBody Cart cart) {
//		if (!userRepository.existsByEmail(email)) {
//			return ResponseEntity.notFound().build();
//		}
//		return ResponseEntity.ok(cartRepository.save(cart));
//	}

}
