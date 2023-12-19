package com.fpt.duantn.controller;


import com.fpt.duantn.domain.Image;
import com.fpt.duantn.domain.Product;
import com.fpt.duantn.dto.DataTablesResponse;
import com.fpt.duantn.service.ImageService;
import com.fpt.duantn.util.FileImgUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

@Controller
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageService imageService;
    @Autowired
    private FileImgUtil fileImgUtil;
    @ResponseBody
    @GetMapping("")
    public DataTablesResponse get(
            @RequestParam(value = "draw", required = false) Optional<Integer> draw,
            @RequestParam(value = "start", required = false) Optional<Integer> start,
            @RequestParam(value = "length", required = false) Optional<Integer> length,
            @RequestParam(value = "search[value]", required = false) Optional<String> searchValue,
            @RequestParam(value = "order[0][column]", required = false) Optional<Integer> orderColumn,
            @RequestParam(value = "order[0][dir]", required = false) Optional<String>  orderDir,
            @RequestParam(value = "idProduct", required = false) Optional<UUID>  idProduct,
            HttpServletRequest request, Model model
    ) {
        String orderColumnName = request.getParameter("columns["+orderColumn.orElse(0)+"][data]");
        Pageable pageable = PageRequest.of(start.orElse(0) / length.orElse(10), length.orElse(10), Sort.by(orderDir.orElse("asc").equals("desc")?Sort.Direction.DESC:Sort.Direction.ASC,orderColumnName==null?"id":orderColumnName));
        Page<Image> images = imageService.findByProductIdAndProductType(idProduct.orElse(null),null,pageable);
        DataTablesResponse response = new DataTablesResponse(draw,images);
        return response;
    }
    @GetMapping("/{id}/")
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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping ( )
    public ResponseEntity<?> add(@Valid @RequestParam("id") UUID idProduct , @RequestPart(value = "imgs",required = false) MultipartFile[] files) {
        FileImgUtil fileImgUtil = new FileImgUtil();
        Product product = new Product();
        product.setId(idProduct);
        List<Image> imagesList= new ArrayList<>();
        for (MultipartFile multipartFile : files){
            try {
                Blob blob =fileImgUtil.convertMultipartFileToBlob(multipartFile);
                Image image = new Image();
                image.setProduct(product);
                image.setImage(blob);
                image.setType(1);
                imagesList.add(image);
            } catch (IOException | SQLException e) {
                ResponseEntity.badRequest().body("Không đọc ghi được ảnh (kiểm tra lại sản phảm vừa tạo)");
            }
        }
        imageService.saveAll(imagesList);
        return ResponseEntity.ok("Thành công");
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping  ("/settype/{id}" )
    public ResponseEntity<?> setType(@PathVariable("id") UUID id , @RequestParam(value = "type",required = true) Integer type) {
       Optional<Image> optional = imageService.findById(id);
        if (!optional.isPresent()){
            return ResponseEntity.badRequest().body("Không tồn tại ảnh");
        }else {
            Image image = optional.get();
            image.setType(type);
            try {
                imageService.save(image);
                return ResponseEntity.ok("Thành công");
            }catch (Exception e){
                return ResponseEntity.ok("Lỗi");
            }
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        if (imageService.existsById(id)){
            imageService.deleteById(id);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().body("Không tồn tại");
        }
    }

}
