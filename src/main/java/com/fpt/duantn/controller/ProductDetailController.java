package com.fpt.duantn.controller;

import com.fpt.duantn.domain.ProductDetail;
import com.fpt.duantn.dto.DataTablesResponse;
import com.fpt.duantn.service.ProductDetailService;
import com.fpt.duantn.service.ProductService;
import com.fpt.duantn.util.FormErrorUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/product-detail")
public class ProductDetailController {

    @Autowired
    private ProductDetailService productDetailService;
    @Autowired
    private ProductService productService;
    @GetMapping()
    @ResponseBody
    public DataTablesResponse getProductDetail(
            @RequestParam(value = "draw", required = false) Optional<Integer> draw,
            @RequestParam(value = "start", required = false) Optional<Integer> start,
            @RequestParam(value = "length", required = false) Optional<Integer> length,
            @RequestParam(value = "search[value]", required = false) Optional<String> searchValue,
            @RequestParam(value = "order[0][column]", required = false) Optional<Integer> orderColumn,
            @RequestParam(value = "order[0][dir]", required = false) Optional<String>  orderDir,
            @RequestParam(value = "idProduct", required = false) Optional<UUID>  idProduct,
            HttpServletRequest request,Model model
    ) {
        String orderColumnName = request.getParameter("columns["+orderColumn.orElse(0)+"][data]");
        Pageable pageable = PageRequest.of(start.orElse(0) / length.orElse(10), length.orElse(10), Sort.by(orderDir.orElse("asc").equals("desc")?Sort.Direction.DESC:Sort.Direction.ASC,orderColumnName==null?"code":orderColumnName));
        Page<ProductDetail> page = productDetailService.searchByKeyAndType(searchValue.orElse(""),null,idProduct.orElse(null), pageable);
        DataTablesResponse response = new DataTablesResponse(draw,page);
        return response;
    }
    @GetMapping("/{id}")
    public ResponseEntity getCategoryById(@PathVariable UUID id) {
        if (productDetailService.existsById(id)){
            return ResponseEntity.ok( productDetailService.findById(id).get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping( value = "/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @ModelAttribute ProductDetail productDetail , BindingResult bindingResult) {
        if (productDetail.getColor()==null){
            bindingResult.rejectValue("color.id","required","Vui lòng nhập trường này");
        }else {
            if (productDetail.getColor().getId()==null){
                bindingResult.rejectValue("color.id","required","Vui lòng nhập trường này");
            };
        }
        if (productDetail.getSize()==null){
            bindingResult.rejectValue("size.id","required","Vui lòng nhập trường này");
        }else {
            if (productDetail.getSize().getId()==null){
                bindingResult.rejectValue("size.id","required","Vui lòng nhập trường này");
            };
        }

        if (bindingResult.hasErrors()){
            Map errors = FormErrorUtil.changeToMapError(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }

        if (productDetailService.existsById(id)){
            Optional<ProductDetail> optionalProductDetail = productDetailService.findById(id);
            ProductDetail productDetailDB = optionalProductDetail.get();
            if (!(productDetailDB.getColor().getId().equals(productDetail.getColor().getId())&&productDetailDB.getSize().getId().equals(productDetail.getSize().getId()))){
                if (productDetailService.existsByProductIdAndColorIdAndSizeId(productDetail.getProduct().getId(),productDetail.getColor().getId(),productDetail.getSize().getId())){
                    return ResponseEntity.badRequest().body("Biến thể này đã tồn tại");
                }
            }
            productDetail.setId(id);
            ProductDetail productDetailSaved = productDetailService.save(productDetail);
            return ResponseEntity.ok(productDetailSaved);

        }else {
            return ResponseEntity.badRequest().body("Không tồn tại");
        }


    }

    @PostMapping ()
    public ResponseEntity<?> add(@Valid @ModelAttribute ProductDetail productDetail , BindingResult bindingResult) {
        if (productDetail.getColor()==null){
            bindingResult.rejectValue("color.id","required","Vui lòng nhập trường này");
        }else {
            if (productDetail.getColor().getId()==null){
                bindingResult.rejectValue("color.id","required","Vui lòng nhập trường này");
            };
        }
        if (productDetail.getSize()==null){
            bindingResult.rejectValue("size.id","required","Vui lòng nhập trường này");
        }else {
            if (productDetail.getSize().getId()==null){
                bindingResult.rejectValue("size.id","required","Vui lòng nhập trường này");
            };
        }

        if (bindingResult.hasErrors()){
            Map errors = FormErrorUtil.changeToMapError(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }
        if (productDetailService.existsByProductIdAndColorIdAndSizeId(productDetail.getProduct().getId(),productDetail.getColor().getId(),productDetail.getSize().getId())){
            return ResponseEntity.badRequest().body("Biến thể này đã tồn tại");
        }
        productDetail.setId(null);
        ProductDetail productDetailSaved = productDetailService.save(productDetail);
        return ResponseEntity.ok(productDetailSaved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        if (productDetailService.existsById(id)){
            try {
                productDetailService.deleteById(id);
                return ResponseEntity.ok().build();
            }catch (DataIntegrityViolationException exception){
                return ResponseEntity.badRequest().body("Không thể xóa khi (đã được sử dụng)");
            }
        }else {
            return ResponseEntity.badRequest().body("Không tồn tại");
        }
    }
}

