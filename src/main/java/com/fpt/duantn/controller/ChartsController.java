package com.fpt.duantn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/charts")
public class ChartsController {
    @GetMapping("/view")
    public String viewThongKeDoanhThu(){
        return "/admin/view/charts/charts-dt";
    }
}
