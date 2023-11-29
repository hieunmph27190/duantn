package com.fpt.duantn.repository;

import com.fpt.duantn.domain.Cart;
import com.fpt.duantn.domain.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartdetailRepository extends JpaRepository<CartDetail,Long> {

    List<CartDetail>findByCart(Cart cart);

    void deleteByCart(Cart cart);
}
