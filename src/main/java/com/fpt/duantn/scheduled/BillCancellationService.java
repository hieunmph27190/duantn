package com.fpt.duantn.scheduled;


import com.fpt.duantn.domain.Bill;
import com.fpt.duantn.domain.BillDetail;
import com.fpt.duantn.domain.ProductDetail;
import com.fpt.duantn.payload.response.MessageResponse;
import com.fpt.duantn.repository.BillDetailRepository;
import com.fpt.duantn.repository.BillRepository;
import com.fpt.duantn.repository.ProductDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillCancellationService {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillDetailRepository billDetailRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Transactional
    @Scheduled(fixedRate = 300000,initialDelay = 60000)
    public void cancelPendingOrders() {
        List<Bill> pendingOrders = billRepository.findByPaymentTypeAndTypeAndBillCreateDateBefore(
                2,-2,
                LocalDateTime.now().minusMinutes(1)
        );
        System.out.println("SYSTEM : "+  LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"))+" :  Quét bill chờ thanh toán");
        for (Bill bill: pendingOrders) {
                 bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo()+"\n\n")+" : SYSTEM : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")));

                try {
                    if (bill.getPaymentAmount()==null||bill.getPaymentAmount().doubleValue()==0){
                        List<BillDetail> billDetails = billDetailRepository.findByBillIdAndType(bill.getId(),1);
                        List<ProductDetail> productDetails2  = new ArrayList<>();
                        for (BillDetail billDetail:billDetails) {
                            ProductDetail productDetail = billDetail.getProductDetail();
                            productDetail.setAmount(productDetail.getAmount()+billDetail.getQuantity());
                            productDetails2.add(productDetail);
                        }
                        bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                                + "\nCộng lại số lượng vào sản phẩm");
                        bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo())
                                +"\nSet Type : "+bill.getType()+" -> "+0);
                        bill.setType(0);
                        productDetailRepository.saveAll(productDetails2);
                        billRepository.save(bill);
                        System.out.println("Bill : "+ bill.getId()+" đã hủy do chưa thanh toán quá 35 phút ");
                    }else {
                        System.out.println("Bill : "+ bill.getId()+" đã được thanh toán ở trạng thái chờ thanh toán");
                    }
                }catch (Exception e){
                    System.out.println("Bill : "+ bill.getId()+" Lỗi hủy đơn");
                }

        }
    }
}

