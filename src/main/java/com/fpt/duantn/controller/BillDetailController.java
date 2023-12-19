package com.fpt.duantn.controller;

import com.fpt.duantn.domain.BillDetail;
import com.fpt.duantn.domain.Employee;
import com.fpt.duantn.dto.BillDetailReponse;
import com.fpt.duantn.dto.DataTablesResponse;
import com.fpt.duantn.dto.EmployeeReponse;
import com.fpt.duantn.service.BillDetailService;
import com.fpt.duantn.service.EmployeeService;
import com.fpt.duantn.service.RoleService;
import com.fpt.duantn.util.FileImgUtil;
import com.fpt.duantn.util.FormErrorUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
@RequestMapping("/bill-detail")
public class BillDetailController {

    @Autowired
    private BillDetailService billDetailService;

    @GetMapping()
    @ResponseBody
    public DataTablesResponse getBillDeTailByBillDetailID(
            @RequestParam(value = "draw", required = false) Optional<Integer> draw,
            @RequestParam(value = "start", required = false) Optional<Integer> start,
            @RequestParam(value = "length", required = false) Optional<Integer> length,
            @RequestParam(value = "search[value]", required = false) Optional<String> searchValue,
            @RequestParam(value = "order[0][column]", required = false) Optional<Integer> orderColumn,
            @RequestParam(value = "order[0][dir]", required = false) Optional<String>  orderDir,
            @RequestParam(value = "callAll", required = false,defaultValue = "false") Optional<Boolean> all,
            @RequestParam(value = "billId", required = true) Optional<UUID> billId,
            HttpServletRequest request, Model model
    ) {
        String orderColumnName = request.getParameter("columns["+orderColumn.orElse(-1)+"][data]");
//        Sort.Order order = new Sort.Order(orderDir.orElse("asc").equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, orderColumnName == null ? "id" : orderColumnName);
//        Sort.Order createDateOrder = new Sort.Order(Sort.Direction.DESC, "createDate");
//        Sort.by(order, createDateOrder);

        Pageable pageable = PageRequest.of(start.orElse(0) / length.orElse(10), length.orElse(10), Sort.by(orderDir.orElse("desc").equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderColumnName == null ? "id" : orderColumnName));
        Page<BillDetailReponse> page = billDetailService.searchByKeyword(searchValue.orElse(""),all.get()?null:1,billId.orElse(null), pageable);
        DataTablesResponse response = new DataTablesResponse(draw, page);
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity getBillDetailById(@PathVariable UUID id) {
        if (billDetailService.existsById(id)){
            BillDetail billDetail = billDetailService.findById(id).get();
            return ResponseEntity.ok(billDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PutMapping ("/{id}")
//    public ResponseEntity<?> updateEmployee(@PathVariable("id") UUID id, @Valid @ModelAttribute Employee employee, BindingResult bindingResult, @RequestPart(value = "images",required = false) MultipartFile[] files) {
//        if (bindingResult.hasErrors()){
//            Map<String, String> errors = FormErrorUtil.changeToMapError(bindingResult);
//            return ResponseEntity.badRequest().body(errors);
//        }
//        employee.setId(id);
//        if (files!=null){
//            if (files.length>0){
//                try {
//                    FileImgUtil fileImgUtil = new FileImgUtil();
//                    Blob blob =fileImgUtil.convertMultipartFileToBlob(files[0]);
//                    if (blob!=null){
//                        employeeService.updateEmployeeImage(employee.getId(),blob);
//                    }
//                } catch (IOException |SQLException e) {
//                    return ResponseEntity.badRequest().body("Không đọc ghi được ảnh (kiểm tra lại nhân viên vừa tạo)");
//                }
//            }
//        }
//        employee.setRole(roleService.findByCode("NV"));
//        Employee oldEmployee =  employeeService.findById(employee.getId()).orElse(null);
//        if (oldEmployee!=null){
//            employee.setPassword(oldEmployee.getPassword());
//        }
//        employeeService.updateEmployeeWithoutImage(employee);
//        Employee employeeSaved =employeeService.findById(employee.getId()).get();
//        return ResponseEntity.ok(employeeSaved);
//    }
//
//    @PostMapping ( )
//    public ResponseEntity<?> addEmployee(@Valid @ModelAttribute Employee employee, BindingResult bindingResult, @RequestPart(value = "images",required = false) MultipartFile[] files) {
//        if (bindingResult.hasErrors()){
//            Map<String, String> errors = FormErrorUtil.changeToMapError(bindingResult);
//            return ResponseEntity.badRequest().body(errors);
//        }
//        employee.setId(null);
//        if (files!=null){
//            if (files.length>0){
//                try {
//                    FileImgUtil fileImgUtil = new FileImgUtil();
//                    Blob blob =fileImgUtil.convertMultipartFileToBlob(files[0]);
//                    if (blob!=null){
//                        employee.setImage(blob);
//                    }
//                } catch (IOException |SQLException e) {
//                    return ResponseEntity.badRequest().body("Không đọc ghi được ảnh (kiểm tra lại nhân viên vừa tạo)");
//                }
//            }
//        }
//        employee.setRole(roleService.findByCode("NV"));
//        Employee oldEmployee =  employeeService.findById(employee.getId()).orElse(null);
//        if (oldEmployee!=null){
//            employee.setPassword(oldEmployee.getPassword());
//        }
//        Employee employeeSaved = employeeService.save(employee);
//        return ResponseEntity.ok(employeeSaved);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity deleteEmployee(@PathVariable UUID id) {
//        if (employeeService.existsById(id)){
//            try {
//                employeeService.deleteById(id);
//                return ResponseEntity.ok().build();
//            } catch (DataIntegrityViolationException exception) {
//                return ResponseEntity.badRequest().body("Không thể xóa khi (đã có dữ liệu sử dụng)");
//            }
//        } else {
//            return ResponseEntity.badRequest().body("Không tồn tại");
//        }
//    }
//    @GetMapping("/{id}/image")
//    public ResponseEntity<?> getImage(@PathVariable UUID id) {
//        FileImgUtil fileImgUtil = new FileImgUtil();
//        Optional<Blob> image = employeeService.findImageById(id);
//        if (!image.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại");
//        }
//        byte[] imageBytes = new byte[0];
//        try {
//            imageBytes = fileImgUtil.convertBlobToByteArray(image.get());
//        } catch (SQLException | IOException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lỗi đọc ảnh");
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_JPEG);
//        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
//    }
}