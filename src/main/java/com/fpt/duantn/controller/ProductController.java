package com.fpt.duantn.controller;


import com.fpt.duantn.domain.Image;
import com.fpt.duantn.domain.Product;
import com.fpt.duantn.domain.ProductDetail;
import com.fpt.duantn.domain.Size;
import com.fpt.duantn.dto.DataTablesResponse;
import com.fpt.duantn.dto.ProductFilterRequest;
import com.fpt.duantn.dto.ProductRequest;
import com.fpt.duantn.dto.ProductResponse;
import com.fpt.duantn.service.ImageService;
import com.fpt.duantn.service.ProductDetailService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

@Controller
@RequestMapping("/product")
public class ProductController {
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/view")
    public String view(Model model){
        return "/admin/view/product/product";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/view-add")
    public String viewAdd(Model model){
        return "/admin/view/product/add-product";
    }

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private FileImgUtil fileImgUtil;

    @Autowired
    private ImageService imageService;



    @GetMapping()
    @ResponseBody
    public DataTablesResponse getProduct(
            @RequestParam(value = "draw", required = false) Optional<Integer> draw,
            @RequestParam(value = "start", required = false) Optional<Integer> start,
            @RequestParam(value = "length", required = false,defaultValue = "10") Optional<Integer> length,
            @RequestParam(value = "search[value]", required = false) Optional<String> searchValue,
            @RequestParam(value = "order[0][column]", required = false) Optional<Integer> orderColumn,
            @RequestParam(value = "order[0][dir]", required = false) Optional<String>  orderDir,
            @ModelAttribute() ProductFilterRequest productFilterRequest,
            @RequestParam(value = "type", required = false) Optional<Integer> type,
            HttpServletRequest request,Model model
    ) {
        String orderColumnName = request.getParameter("columns["+orderColumn.orElse(-1)+"][data]");
        Pageable pageable = PageRequest.of((int) (start.orElse(0) / length.orElse(10)), length.orElse(10),  Sort.by(orderDir.orElse("desc").equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderColumnName == null ? "createDate" : orderColumnName));
        Integer typeInt = type.orElse(-1);
        Page<Product> page = productService.searchByKeyAndTypeAndFilter(searchValue.orElse(""),typeInt==-1?null:typeInt,productFilterRequest, pageable);
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





//    @GetMapping()
//    @ResponseBody
//    public DataTablesResponse getProduct(
//            @RequestParam(value = "draw", required = false) Optional<Integer> draw,
//            @RequestParam(value = "start", required = false) Optional<Integer> start,
//            @RequestParam(value = "length", required = false) Optional<Integer> length,
//            @RequestParam(value = "search[value]", required = false) Optional<String> searchValue,
//            @RequestParam(value = "order[0][column]", required = false) Optional<Integer> orderColumn,
//            @RequestParam(value = "order[0][dir]", required = false) Optional<String>  orderDir,
//            @ModelAttribute() ProductFilterRequest productFilterRequest,
//            HttpServletRequest request,Model model
//    ) {
//        String orderColumnName = request.getParameter("columns["+orderColumn.orElse(-1)+"][data]");
//        Pageable pageable = PageRequest.of(start.orElse(0) / length.orElse(10), length.orElse(10),  Sort.by(orderDir.orElse("desc").equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderColumnName == null ? "code" : orderColumnName));
//        Page<ProductResponse> page = productService.searchResponseByKeyAndTypeAndFilter(searchValue.orElse(""),null, pageable);
//        DataTablesResponse response = new DataTablesResponse(draw,page);
//        response.setData(page.getContent());
//        return response;
//    }




    @GetMapping("/{id}")
    public ResponseEntity getProductById(@PathVariable UUID id) {
        if (productService.existsById(id)){
            return ResponseEntity.ok( productService.findById(id).get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping( value = "/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @ModelAttribute Product product , BindingResult bindingResult) {
        if (!productService.existsById(id)){
            bindingResult.rejectValue("id","","Id này không tồn tại");
        }
        if (bindingResult.hasErrors()){
            Map errors = FormErrorUtil.changeToMapError(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }

        Product existingProduct = productService.findById(id).orElse(null);
        if ( productService.findByCode(product.getCode()) != null&&(!existingProduct.getCode().equals(product.getCode()))) {
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "Mã đã tồn tại");
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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping ( )
    public ResponseEntity<?> add(@Valid @ModelAttribute Product product , BindingResult bindingResult, @RequestPart(value = "imgs",required = false) MultipartFile[] files) {
        if (bindingResult.hasErrors()){
            Map errors = FormErrorUtil.changeToMapError(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }
        // Kiểm tra mã trùng
        Product existingProduct = productService.findByCode(product.getCode());
        if (existingProduct != null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "Mã đã tồn tại");
            return ResponseEntity.badRequest().body(errors);
        }

        product.setId(null);
        Product productSaved = productService.save(product);
        List<Image> imagesList= new ArrayList<>();
        boolean imgSelect = true;
        for (MultipartFile multipartFile : files){
            try {
                Blob blob =fileImgUtil.convertMultipartFileToBlob(multipartFile);
                if (blob!=null){
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
                }
            } catch (IOException |SQLException e) {
                return ResponseEntity.badRequest().body("Không đọc ghi được ảnh (kiểm tra lại sản phảm vừa tạo)");
            }
        }


        imageService.saveAll(imagesList);
        return ResponseEntity.ok(productSaved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping ( value = "/add" )
    public ResponseEntity<?> addProduct(@Valid @ModelAttribute ProductRequest productRequest , BindingResult bindingResult, @RequestPart(value = "imgs",required = false) MultipartFile[] files) {

        if (bindingResult.hasErrors()){
            Map errors = FormErrorUtil.changeToMapError(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }

        // Kiểm tra mã trùng
        Product existingProduct = productService.findByCode(productRequest.getCode());
        if (existingProduct != null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "Mã đã tồn tại");
            return ResponseEntity.badRequest().body(errors);
        }


//        Conver dữ liệu sang domain
        Product product = new Product();
        product.setCode(productRequest.getCode());
        product.setName(productRequest.getName());
        product.setType(productRequest.getType());
        product.setBrand(productRequest.getBrand());
        product.setCategory(productRequest.getCategory());
        product.setSole(productRequest.getSole());
        product.setDescription(productRequest.getDescription());

        Product productSaved = productService.save(product);

       try{
           List<ProductDetail> productDetails = productRequest.getDetails();
           productDetails.stream().forEach(item ->{
               item.setProduct(product);
           });

           productDetailService.saveAll(productDetails);

           //        Thêm ảnh
           List<Image> imagesList= new ArrayList<>();
           boolean imgSelect = true;
           for (MultipartFile multipartFile : files){
               try {
                   Blob blob =fileImgUtil.convertMultipartFileToBlob(multipartFile);

                   if (blob!=null){
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
                   }


               } catch (IOException |SQLException e) {
                   return ResponseEntity.badRequest().body("Không đọc ghi được ảnh (kiểm tra lại sản phảm vừa tạo)");
               }
           }
           imageService.saveAll(imagesList);

       }catch (Exception e){
           return ResponseEntity.badRequest().body("Có lỗi sảy ra (kiểm tra lại sản phảm vừa tạo)");
       }


        return ResponseEntity.ok(productSaved);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        if (productService.existsById(id)){
            try {
                productService.deleteById(id);
                return ResponseEntity.ok().build();
            }catch (DataIntegrityViolationException exception){
                return ResponseEntity.badRequest().body("Không thể xóa khi (đã sử dụng)");
            }
        }else {
            return ResponseEntity.badRequest().body("Không tồn tại");
        }
    }


    @GetMapping("/imageID/{id}")
    public ResponseEntity<?> getProductIDImage(@PathVariable UUID id) {
        List<UUID> ids = imageService.findIDByProductId(id,1);
        return ResponseEntity.ok(ids);
    }
    @GetMapping("/image/{id}")
    public ResponseEntity<?> getProductImage(@PathVariable UUID id) {
        List<Image> images = imageService.findByProductIdAndProductType(id,null);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{productId}/image-main")
    public ResponseEntity<?> getImageMain(@PathVariable UUID productId) {
        List<Image> ids =  imageService.findByProductId(productId,2);
        if (ids.size()<=0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại");
        }

        Image image = ids.get(0);
        byte[] imageBytes = new byte[0];
        try {
            imageBytes = fileImgUtil.convertBlobToByteArray(image.getImage());
        } catch (SQLException |IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lỗi đọc ảnh");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
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

