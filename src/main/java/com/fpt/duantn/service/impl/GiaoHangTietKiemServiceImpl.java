package com.fpt.duantn.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.duantn.dto.TransportationFeeDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class GiaoHangTietKiemServiceImpl {
    String apiToken = "bc002d03-9cf5-11ee-96dc-de6f804954c9";
    String idCH = "3440";

    private static final String API_BASE_URL = "https://online-gateway.ghn.vn/shiip/public-api/master-data/";


    public ResponseEntity tinhShip(String tinh,String huyen,String xa,Integer quantity,Long price) throws Exception {
        String idTinh = getIDTinh(tinh);
        String idHuyen = getIDHuyen(huyen,idTinh);
        String idXa = getIDXa(xa,idHuyen);
        Map<String,String> map = new HashMap<>();
        map.put("ProvinceID",idTinh);
        map.put("DistrictID",idHuyen);
        map.put("WardCode",idXa);

        try {
            RestTemplate restTemplate = new RestTemplate();

            // Tạo tiêu đề
            HttpHeaders headers = new HttpHeaders();
            headers.set("token", apiToken);
            headers.set("Content-Type", "application/json"); // Thay thế tên và giá trị của tiêu đề cần thiết

            // Tạo đối tượng HttpEntity chứa tiêu đề
            HttpEntity<String> entity = new HttpEntity<>(headers);
            DecimalFormat df = new DecimalFormat("#");
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee?service_type_id=2&from_district_id=3440&to_district_id=" +idCH + "&to_ward_code=" + idXa + "&height=" + TransportationFeeDTO.heightProduct + "&length=" + TransportationFeeDTO.lengthProduct + "&weight=" + TransportationFeeDTO.weightProduct * quantity + "&width=" + TransportationFeeDTO.widthProduct * quantity + "&insurance_value=" + df.format(price), HttpMethod.GET, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.badRequest().body("Không thành công");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Không thành công");
        }
    }

    private String getIDTinh(String tinh) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("token", apiToken);
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(API_BASE_URL + "province", HttpMethod.GET, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode jsonNodex = objectMapper.readTree(response.getBody());
                JsonNode jsonNode = jsonNodex.get("data");
                for (JsonNode node : jsonNode) {
                    boolean check = false;
                    JsonNode nameExtensionNode = node.get("NameExtension");
                    if (nameExtensionNode != null && nameExtensionNode.isArray()) {
                        for (JsonNode nameNode : nameExtensionNode) {
                            String name = nameNode.asText();
                            if (name.equalsIgnoreCase(tinh)){
                                check = true;
                                break;
                            }
                        }
                    }
                    if (check){
                        return node.get("ProvinceID").asText();
                    }
                }
            } else {
                throw new Exception("Lỗi lấy địa chỉ Giao Hàng Nhanh");
            }
        } catch (HttpClientErrorException e) {
            throw new Exception("Lỗi lấy địa chỉ Giao Hàng Nhanh");
        } catch (JsonProcessingException e) {
            throw new Exception("Lỗi Sử lí dữ liệu Giao Hàng Nhanh");
        }
        throw new Exception("Không tìm thấy địa chỉ");
    }
    private String getIDHuyen(String huyen,String provinceId) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("token", apiToken);
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(API_BASE_URL + "district?province_id=" + provinceId, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode jsonNodex = objectMapper.readTree(response.getBody());
                JsonNode jsonNode = jsonNodex.get("data");
                for (JsonNode node : jsonNode) {
                    boolean check = false;
                    JsonNode nameExtensionNode = node.get("NameExtension");
                    if (nameExtensionNode != null && nameExtensionNode.isArray()) {
                        for (JsonNode nameNode : nameExtensionNode) {
                            String name = nameNode.asText();
                            if (name.equalsIgnoreCase(huyen)){
                                check = true;
                                break;
                            }
                        }
                    }
                    if (check){
                        return node.get("DistrictID").asText();
                    }
                }
            } else {
                throw new Exception("Lỗi lấy địa chỉ Giao Hàng Nhanh");
            }

        } catch (HttpClientErrorException e) {
            throw new Exception("Lỗi lấy địa chỉ Giao Hàng Nhanh");
        } catch (JsonProcessingException e) {
            throw new Exception("Lỗi Sử lí dữ liệu Giao Hàng Nhanh");
        }
        throw new Exception("Không tìm thấy địa chỉ");
    }
    private String getIDXa(String xa,String districtId) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("token", apiToken);
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(API_BASE_URL + "ward?district_id=" + districtId, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode jsonNodex = objectMapper.readTree(response.getBody());
                JsonNode jsonNode = jsonNodex.get("data");
                for (JsonNode node : jsonNode) {
                    boolean check = false;
                    JsonNode nameExtensionNode = node.get("NameExtension");
                    if (nameExtensionNode != null && nameExtensionNode.isArray()) {
                        for (JsonNode nameNode : nameExtensionNode) {
                            String name = nameNode.asText();
                            if (name.equalsIgnoreCase(xa)){
                                check = true;
                                break;
                            }
                        }
                    }
                    if (check){
                        return node.get("WardCode").asText();
                    }
                }
            } else {
                throw new Exception("Lỗi lấy địa chỉ Giao Hàng Nhanh");
            }

        } catch (HttpClientErrorException e) {
            System.out.println(e);
            throw new Exception("Lỗi lấy địa chỉ Giao Hàng Nhanh");
        } catch (JsonProcessingException e) {
            throw new Exception("Lỗi Sử lí dữ liệu Giao Hàng Nhanh");
        }
        throw new Exception("Không tìm thấy địa chỉ");
    }

}
