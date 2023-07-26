package com.fpt.duantn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.duantn.domain.Color;
import com.fpt.duantn.domain.Image;
import com.fpt.duantn.domain.Product;
import com.fpt.duantn.model.DataTablesResponse;
import com.fpt.duantn.model.ProductResponse;
import com.fpt.duantn.service.ColorService;
import com.fpt.duantn.service.ImageService;
import com.fpt.duantn.service.ProductService;
import com.fpt.duantn.util.FileImgUtil;
import com.fpt.duantn.util.FormErrorUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/product")
public class ProductController {
    @GetMapping("/view")
    public String test(Model model){
        return "/admin/view/product/product";
    }

    @Autowired
    private ProductService productService;

    @Autowired
    private FileImgUtil fileImgUtil;

    @Autowired
    private ImageService imageService;

    @GetMapping()
    @ResponseBody
    public DataTablesResponse getProduct(
            @RequestParam(value = "draw", required = false) Optional<Integer> draw,
            @RequestParam(value = "start", required = false) Optional<Integer> start,
            @RequestParam(value = "length", required = false) Optional<Integer> length,
            @RequestParam(value = "search[value]", required = false) Optional<String> searchValue,
            @RequestParam(value = "order[0][column]", required = false) Optional<Integer> orderColumn,
            @RequestParam(value = "order[0][dir]", required = false) Optional<String>  orderDir,
            HttpServletRequest request,Model model
    ) {
        String orderColumnName = request.getParameter("columns["+orderColumn.orElse(0)+"][data]");
        Pageable pageable = PageRequest.of(start.orElse(0) / length.orElse(10), length.orElse(10), Sort.by(orderDir.orElse("asc").equals("desc")?Sort.Direction.DESC:Sort.Direction.ASC,orderColumnName==null?"code":orderColumnName));
        Page<Product> page = productService.searchByKeyAndType(searchValue.orElse(""),null, pageable);
        DataTablesResponse response = new DataTablesResponse(draw,page);
        List<Product> products = (List<Product>) response.getData();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product:products) {
            ProductResponse productResponse = new ProductResponse(product);
            List<UUID> imageIds = imageService.findIDByProductId(product.getId(),2);
            if (imageIds.size()>0){
                Random random = new Random();
                UUID imageId = imageIds.get(random.nextInt(0,imageIds.size()));
                productResponse.setImageId(imageId);
            }
            productResponses.add(productResponse);
        }
        response.setData(productResponses);
        return response;
    }
    @GetMapping("/{id}")
    public ResponseEntity getProductById(@PathVariable UUID id) {
        if (productService.existsById(id)){
            return ResponseEntity.ok( productService.findById(id).get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping( value = "/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @ModelAttribute Product product , BindingResult bindingResult) {
        if (!productService.existsById(id)){
            bindingResult.rejectValue("id","","Id này không tồn tại");
        }
        if (bindingResult.hasErrors()){
            Map errors = FormErrorUtil.changeToMapError(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }
        if (productService.existsById(id)){
            product.setId(id);
            Product productSaved = productService.save(product);
            return ResponseEntity.ok(productSaved);
        }else {
            return ResponseEntity.badRequest().body("Không tồn tại");
        }


    }

    @PostMapping ( )
    public ResponseEntity<?> add(@Valid @ModelAttribute Product product , BindingResult bindingResult, @RequestPart(value = "imgs",required = false) MultipartFile[] files) {
        if (bindingResult.hasErrors()){
            Map errors = FormErrorUtil.changeToMapError(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }
        product.setId(null);
        Product productSaved = productService.save(product);
        List<Image> imagesList= new ArrayList<>();
        boolean imgSelect = true;
        for (MultipartFile multipartFile : files){
            try {
                Blob blob =fileImgUtil.convertMultipartFileToBlob(multipartFile);
                Image image = new Image();
                image.setProduct(productSaved);
                image.setImage(blob);
                if (imgSelect){
                    image.setType(2);
                    imgSelect=false;
                }else {
                    image.setType(1);
                }
                imagesList.add(image);
            } catch (IOException |SQLException e) {
                ResponseEntity.badRequest().body("Không đọc ghi được ảnh (kiểm tra lại sản phảm vừa tạo)");
            }
        }
        imageService.saveAll(imagesList);
        return ResponseEntity.ok(productSaved);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        if (productService.existsById(id)){
            try {
                imageService.deleteAllById(imageService.findIDByProductId(id,null));
                productService.deleteById(id);
                return ResponseEntity.ok().build();
            }catch (DataIntegrityViolationException exception){
                return ResponseEntity.badRequest().body("Không thể xóa khi (đã có sản phẩm sử dụng)");
            }
        }else {
            return ResponseEntity.badRequest().body("Không tồn tại");
        }
    }


    @GetMapping("/imageID/{id}")
    public ResponseEntity<?> getProductIDImage(@PathVariable UUID id) {
        List<UUID> ids = imageService.findIDByProductId(id,null);
        return ResponseEntity.ok(ids);
    }
    @GetMapping("/image/{id}")
    public ResponseEntity<?> getProductImage(@PathVariable UUID id) {
        List<Image> images = imageService.findByProductIdAndAndProductType(id,null);
        return ResponseEntity.ok(images);
    }


    @GetMapping("/{id}/image")
    public ResponseEntity<?> getImage(@PathVariable UUID id) {
        Optional<Image> image = imageService.findById(id);
        if (!image.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại");
        }
        byte[] imageBytes = new byte[0];
        try {
            imageBytes = fileImgUtil.convertBlobToByteArray(image.get().getImage());
        } catch (SQLException |IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lỗi đọc ảnh");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}

