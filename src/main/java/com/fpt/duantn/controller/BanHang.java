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
        return "Trangchu/about";
    }

    @GetMapping("/blog")
    public String blog() {
        return "Trangchu/blog";
    }

    @GetMapping("/cart")
    public String cart() {
        return "Trangchu/cart";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "Trangchu/checkout";
    }

    @GetMapping("/contact")
    public String contact() {
        return "Trangchu/contact";
    }

    @GetMapping("/shop")
    public String shop() {
        return "Trangchu/shop";
    }

    @GetMapping("/blogsingle")
    public String blogsingle() {
        return "Trangchu/blog-single";
    }



}
