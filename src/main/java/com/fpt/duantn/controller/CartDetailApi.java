

package com.fpt.duantn.controller;

import com.fpt.duantn.domain.CartDetail;
import com.fpt.duantn.domain.ProductDetail;
import com.fpt.duantn.repository.CartRepository;
import com.fpt.duantn.repository.CartdetailRepository;
import com.fpt.duantn.repository.ProductDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/cartDetail")
public class CartDetailApi {

	@Autowired
	CartdetailRepository cartDetailRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	ProductDetailRepository productDetailRepository;

	@GetMapping("cart/{id}")
	public ResponseEntity<List<CartDetail>> getByCartId(@PathVariable("id") Long id) {
		if (!cartRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(cartDetailRepository.findByCart(cartRepository.findById(id).get()));
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public ResponseEntity<CartDetail> getOne(@PathVariable("id") Long id) {
		if (!cartDetailRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(cartDetailRepository.findById(id).get());
	}



	@PostMapping()
	public ResponseEntity<CartDetail> post(@RequestBody CartDetail detail) {
		if (!cartRepository.existsById(detail.getCart().getCart_id())) {
			return ResponseEntity.notFound().build();
		}
		ProductDetail productdetail = productDetailRepository.findById(detail.getProductdetail().getId()).orElse(null);

		if (productdetail==null) {
			return ResponseEntity.notFound().build();
		}

		List<CartDetail> listD = cartDetailRepository.
				findByCart(cartRepository.findById(detail.getCart().getCart_id()).get());
		for (CartDetail item : listD) {
			if (item.getProductdetail().getId().equals(detail.getProductdetail().getId())) {
				item.setQuantity(item.getQuantity() + 1);
				item.setPrice(item.getPrice() + detail.getPrice());
				return ResponseEntity.ok(cartDetailRepository.save(item));
			}
		}
		return ResponseEntity.ok(cartDetailRepository.save(detail));
	}





	@PutMapping()
	public ResponseEntity<CartDetail> put(@RequestBody CartDetail detail) {
		if (!cartRepository.existsById(detail.getCart().getCart_id())) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(cartDetailRepository.save(detail));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		if (!cartDetailRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		cartDetailRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
