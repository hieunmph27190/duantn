

package com.fpt.duantn.controller;

import com.fpt.duantn.domain.CartDetail;
import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.domain.ProductDetail;
import com.fpt.duantn.models.User;
import com.fpt.duantn.repository.CartdetailRepository;
import com.fpt.duantn.repository.ProductDetailRepository;
import com.fpt.duantn.security.services.AuthenticationService;
import com.fpt.duantn.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@PreAuthorize("hasRole('USER')")
@Controller
@RequestMapping("api/cart")
public class CartDetailApi {

	@Autowired
	CartdetailRepository cartDetailRepository;
	@Autowired
	CustomerService customerService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	ProductDetailRepository productDetailRepository;

	@GetMapping("/cart-detail/{id}")
	public ResponseEntity<CartDetail> getByCartDetail(@PathVariable("id") Long id) {
		if (!cartDetailRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		CartDetail cartDetail = cartDetailRepository.findById(id).get();
		cartDetail.setCustomer(null);
		return ResponseEntity.ok(cartDetail);
	}

	@RequestMapping(value = "/cart-details", method = RequestMethod.GET)
	public ResponseEntity<?> getCartDetails( Authentication authentication) {
		User user = authenticationService.loadUserByUsername(authentication.getName());
		if (!customerService.existsById(user.getId())) {
			return ResponseEntity.badRequest().body("Cần đăng nhập đúng tài khoản khách hàng");
		}
		List<CartDetail> cartDetails = cartDetailRepository.findByCustomerIdAndType(user.getId(),1);
		for (CartDetail cartDetail:cartDetails) {
			cartDetail.setCustomer(null);
		}
		return ResponseEntity.ok(cartDetails);
	}



	@PostMapping()
	public ResponseEntity<?> post(@RequestParam UUID productDetailId,Authentication authentication) {
		User user = authenticationService.loadUserByUsername(authentication.getName());
		if (!customerService.existsById(user.getId())) {
			return ResponseEntity.badRequest().body("Cần đăng nhập đúng tài khoản khách hàng");
		}
		if (cartDetailRepository.existsByCustomerIdAndProductDetailIdAndType(user.getId(),productDetailId,1)) {
			return ResponseEntity.badRequest().body("Sản phẩm đã có trong gio hàng");
		}

		CartDetail cartDetail = new CartDetail();
		ProductDetail productDetail = new ProductDetail();
		productDetail.setId(productDetailId);
		cartDetail.setProductDetail(productDetail);

		Customer customer = new Customer();
		customer.setId(user.getId());
		cartDetail.setCustomer(customer);

		cartDetail.setType(1);
		CartDetail cartDetailSaved = cartDetailRepository.save(cartDetail);
		cartDetailSaved.setCustomer(null);

		return ResponseEntity.ok(cartDetailSaved);
	}


	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id,Authentication authentication) {
		User user = authenticationService.loadUserByUsername(authentication.getName());
		if (!customerService.existsById(user.getId())) {
			return ResponseEntity.badRequest().body("Cần đăng nhập đúng tài khoản khách hàng");
		}
		if (!cartDetailRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		int result = cartDetailRepository.deleteByCartDetailIdAndCustomerId(id,user.getId());
		if (result>0){
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.badRequest().body("Xóa không thành công");
		}

	}

}
