package com.fpt.duantn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BanHang {

    @GetMapping("")
    public String home() {
        return "banhang/view/index";
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
