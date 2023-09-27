package com.fpt.duantn.controller;

import com.fpt.duantn.domain.Product;
import com.fpt.duantn.dto.DataTablesResponse;
import com.fpt.duantn.dto.ProductBanHangResponse;
import com.fpt.duantn.dto.ProductResponse;
import com.fpt.duantn.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class BanHangController {
    @Autowired
    private ProductService productService;
    @GetMapping("")
    public String home() {
        return "banhang/view/index";
    }

    @GetMapping("/ban-hang/product")
    @ResponseBody
    public DataTablesResponse getProduct(
            @RequestParam(value = "draw", required = false) Optional<Integer> draw,
            @RequestParam(value = "start", required = false) Optional<Integer> start,
            @RequestParam(value = "length", required = false) Optional<Integer> length,
            @RequestParam(value = "search[value]", required = false) Optional<String> searchValue,
            @RequestParam(value = "order[0][column]", required = false) Optional<Integer> orderColumn,
            @RequestParam(value = "order[0][dir]", required = false) Optional<String>  orderDir,
            HttpServletRequest request, Model model
    ) {
        String orderColumnName = request.getParameter("columns["+orderColumn.orElse(0)+"][data]");
        Pageable pageable = PageRequest.of(start.orElse(0) / length.orElse(10), length.orElse(10));
        Page<ProductBanHangResponse> page = productService.searchResponseByKeyAndType(searchValue.orElse(""),null, pageable);
        DataTablesResponse response = new DataTablesResponse(draw,page);
        return response;
    }

    @GetMapping("/about")
    public String about() {
        return "banhang/view/about";
    }

    @GetMapping("/blog")
    public String blog() {
        return "banhang/view/blog";
    }

    @GetMapping("/cart")
    public String cart() {
        return "banhang/view/cart";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "banhang/view/checkout";
    }

    @GetMapping("/contact")
    public String contact() {
        return "banhang/view/contact";
    }

    @GetMapping("/shop")
    public String shop() {
        return "banhang/view/shop";
    }

    @GetMapping("/blogsingle")
    public String blogsingle() {
        return "banhang/view/blog-single";
    }



}
