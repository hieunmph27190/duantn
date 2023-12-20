package com.fpt.duantn.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.duantn.dto.TransportationFeeDTO;
import com.fpt.duantn.service.impl.GiaoHangTietKiemServiceImpl;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/public")
public class GiaoHangTietKiemController {
    private static final String API_BASE_URL = "https://online-gateway.ghn.vn/shiip/public-api/master-data/";

    @Autowired
        private GiaoHangTietKiemServiceImpl giaoHangTietKiemService;
        @GetMapping("/shipfee")
        public ResponseEntity tinhFee(@RequestParam() String tinh, @RequestParam() String huyen, @RequestParam() String xa,@RequestParam() Integer quantity,@RequestParam() Long price) {
            try {
               return giaoHangTietKiemService.tinhShip(tinh,huyen,xa,quantity,price);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

//        @GetMapping("/districts")
//        @ResponseBody
//        public String getDistrictsByProvince(@RequestParam("province_id") Integer provinceId) {
//            try {
//                RestTemplate restTemplate = new RestTemplate();
//                HttpHeaders headers = new HttpHeaders();
//                headers.set("token", "bc002d03-9cf5-11ee-96dc-de6f804954c9");
//                headers.set("Content-Type", "application/json");
//                HttpEntity<String> entity = new HttpEntity<>(headers);
//                ResponseEntity<String> response = restTemplate.exchange(API_BASE_URL + "district?province_id=" + provinceId, HttpMethod.GET, entity, String.class);
//
//                if (response.getStatusCode().is2xxSuccessful()) {
//                    return response.getBody();
//                } else {
//                    return null;
//                }
//            } catch (HttpClientErrorException e) {
//                return "";
//            }
//        }


        @GetMapping("/wards")
        @ResponseBody
        public String getWardsByDistrict(@RequestParam("district_id") Integer districtId) {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("token", "bc002d03-9cf5-11ee-96dc-de6f804954c9");
                    headers.set("Content-Type", "application/json");
                    HttpEntity<String> entity = new HttpEntity<>(headers);
                    ResponseEntity<String> response = restTemplate.exchange(API_BASE_URL + "ward?district_id=" + districtId, HttpMethod.GET, entity, String.class);

                    if (response.getStatusCode().is2xxSuccessful()) {
                        return response.getBody();
                    } else {
                        return null;
                    }
                } catch (HttpClientErrorException e) {
                    return "";
                }
        }


        @PostMapping("/transportationFee")
        @ResponseBody
        public String getFee(@RequestBody TransportationFeeDTO transportationFeeDTO) {
            try {
                RestTemplate restTemplate = new RestTemplate();

                // Tạo tiêu đề
                HttpHeaders headers = new HttpHeaders();
                headers.set("token", "bc002d03-9cf5-11ee-96dc-de6f804954c9");
                headers.set("Content-Type", "application/json"); // Thay thế tên và giá trị của tiêu đề cần thiết

                // Tạo đối tượng HttpEntity chứa tiêu đề
                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<String> response = restTemplate.exchange(
                        "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee?service_type_id=2&from_district_id=3440&to_district_id=" + transportationFeeDTO.getToDistrictId() + "&to_ward_code=" + transportationFeeDTO.getToWardCode() + "&height=" + TransportationFeeDTO.heightProduct + "&length=" + TransportationFeeDTO.lengthProduct + "&weight=" + TransportationFeeDTO.weightProduct * transportationFeeDTO.getQuantity() + "&width=" + TransportationFeeDTO.widthProduct * transportationFeeDTO.getQuantity() + "&insurance_value=" + transportationFeeDTO.getInsuranceValue(), HttpMethod.GET, entity, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    return response.getBody();
                } else {
                    // Xử lý khi có lỗi
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }
    }


