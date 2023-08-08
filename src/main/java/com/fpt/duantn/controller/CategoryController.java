package com.fpt.duantn.controller;

import com.fpt.duantn.domain.Category;
import com.fpt.duantn.dto.DataTablesResponse;
import com.fpt.duantn.service.CategoryService;
import com.fpt.duantn.util.FormErrorUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @GetMapping("/view")
    public String test(Model model){
        return "/admin/view/category/category";
    }

    @Autowired
    private CategoryService categoryService;
    @GetMapping()
    @ResponseBody
    public DataTablesResponse getCategory(
            @RequestParam(value = "draw", required = false) Optional<Integer> draw,
            @RequestParam(value = "start", required = false) Optional<Integer> start,
            @RequestParam(value = "length", required = false) Optional<Integer> length,
            @RequestParam(value = "search[value]", required = false) Optional<String> searchValue,
            @RequestParam(value = "order[0][column]", required = false) Optional<Integer> orderColumn,
            @RequestParam(value = "order[0][dir]", required = false) Optional<String>  orderDir,
            @RequestParam(value = "callAll", required = false,defaultValue = "false") Optional<Boolean> all,
            HttpServletRequest request,Model model
    ) {
        String orderColumnName = request.getParameter("columns["+orderColumn.orElse(0)+"][data]");
        Pageable pageable = PageRequest.of(start.orElse(0) / length.orElse(10), length.orElse(10), Sort.by(orderDir.orElse("asc").equals("desc")?Sort.Direction.DESC:Sort.Direction.ASC,orderColumnName==null?"code":orderColumnName));
        Page<Category> page = categoryService.searchByKeyAndType(searchValue.orElse(""),all.get()?null:1, pageable);
        DataTablesResponse response = new DataTablesResponse(draw,page);
        return response;
    }
    @GetMapping("/{id}")
    public ResponseEntity getCategoryById(@PathVariable UUID id) {
        if (categoryService.existsById(id)){
            return ResponseEntity.ok( categoryService.findById(id).get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping( value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @RequestBody Category category , BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            Map errors = FormErrorUtil.changeToMapError(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }
        if (categoryService.existsById(id)){
            category.setId(id);
            Category categorySaved = categoryService.save(category);
            return ResponseEntity.ok(categorySaved);
        }else {
            return ResponseEntity.badRequest().body("Không tồn tại");
        }


    }

    @PostMapping ( consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@Valid @RequestBody Category Category , BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            Map errors = FormErrorUtil.changeToMapError(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }
        Category.setId(null);
        Category categorySaved = categoryService.save(Category);
        return ResponseEntity.ok(categorySaved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        if (categoryService.existsById(id)){
            try {
                categoryService.deleteById(id);
                return ResponseEntity.ok().build();
            }catch (DataIntegrityViolationException exception){
                return ResponseEntity.badRequest().body("Không thể xóa khi (đã có sản phẩm sử dụng)");
            }
        }else {
            return ResponseEntity.badRequest().body("Không tồn tại");
        }
    }
}

