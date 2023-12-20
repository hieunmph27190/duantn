package com.fpt.duantn.controller;

import com.fpt.duantn.domain.Bill;
import com.fpt.duantn.domain.BillDetail;
import com.fpt.duantn.domain.ProductDetail;
import com.fpt.duantn.payload.response.MessageResponse;
import com.fpt.duantn.service.BillDetailService;
import com.fpt.duantn.service.BillService;
import com.fpt.duantn.service.ProductDetailService;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.validator.internal.util.Contracts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api")
public class VNPayController {
    @Autowired
    private BillService billService;
    @Autowired
    private BillDetailService billDetailService;
    @Autowired
    private ProductDetailService productDetailService;


    @GetMapping("/payment-callback")
    public void paymentCallback(@RequestParam Map<String, String> queryParams, HttpServletResponse response, Authentication authentication) throws IOException, ParseException {
        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
        String billId = queryParams.get("billId");
        String amount = queryParams.get("vnp_Amount");
        String registerServiceId = queryParams.get("registerServiceId");
        String transactionNo = queryParams.get("vnp_TransactionNo");
        if((billId!= null && !billId.equals(""))||(registerServiceId!= null && !registerServiceId.equals(""))) {
            if ("00".equals(vnp_ResponseCode)) {
                // Giao dịch thành công
                // Thực hiện các xử lý cần thiết, ví dụ: cập nhật CSDL
                Bill bill = billService.findById(UUID.fromString(queryParams.get("billId"))).orElse(null);
//                if (bill==null){
//                    return new MessageResponse("Bill không tồn tại");
//                }

                BigDecimal paymentAmount =  bill.getPaymentAmount()==null?new BigDecimal(0):bill.getPaymentAmount();
                Double newAmount =  paymentAmount.doubleValue()+Double.valueOf(amount)/100;
                bill.setPaymentAmount(BigDecimal.valueOf(newAmount));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date parsedDate = dateFormat.parse(queryParams.get("vnp_PayDate"));
                Timestamp timestamp = new Timestamp(parsedDate.getTime());
                if (bill.getType().equals(-2)&&bill.getPaymentType().equals(1)){
                    bill.setType(7);
                }else if (bill.getType().equals(-2)&&bill.getPaymentType().equals(2)){
                    bill.setType(4);
                }
                bill.setPaymentTime(timestamp);
                bill.setTransactionNo((bill.getTransactionNo()==null?"": bill.getTransactionNo()+"\n\n")+"Đã thanh toán : "+(Double.valueOf(amount)/100)+" : VNP Code : "+transactionNo+" : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")));
                billService.save(bill);
                if (queryParams.get("admin") !=null&&queryParams.get("admin") !=""){
                    response.sendRedirect("http://localhost:8080/payment/success?billId="+billId+"&amount="+amount+"&transactionNo="+transactionNo);
                }else {
                    response.sendRedirect("http://localhost:4200/bill");
                }
            } else {
                // Giao dịch thất bại
                // Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
                if (queryParams.get("admin") !=null&&queryParams.get("admin") !=""){
                    response.sendRedirect("http://localhost:8080/payment/error?billId="+billId+"&transactionNo="+transactionNo);
                }else {
                    response.sendRedirect("http://localhost:4200/payment-failed");
                }


            }
        }else {
            if (queryParams.get("admin") !=null&&queryParams.get("admin") !=""){
                response.sendRedirect("http://localhost:8080/payment/error?billId="+billId+"&transactionNo="+transactionNo);
            }else {
                response.sendRedirect("http://localhost:4200/payment-failed");
            }
        }

    }

	@GetMapping("/vnpay/{billID}")
	public ResponseEntity getPay(@PathVariable UUID billID,@RequestParam(required = false) String admin) throws UnsupportedEncodingException{
        Double totalMoney = billDetailService.sumMoneyByBillIdAndType(billID,1).orElse(null);
        Bill bill = billService.findById(billID).orElse(null);
        Double shipfee =  bill.getShipeFee().doubleValue();
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(340);

        Date now = new Date();
        Timestamp currentTimestamp = new Timestamp(now.getTime());
        long millisecondsDifference = currentTimestamp.getTime() - bill.getBillCreateDate().getTime();
        long minutesDifference = millisecondsDifference / (60 * 1000);
        if ((15-minutesDifference)<0&&bill.getPaymentType().equals(2)){
            return ResponseEntity.badRequest().body("Không thể thanh toán bill đã quá thời gian");
        }

        String vnp_Version =ConfigVNPay.vnp_Version;
        String vnp_Command = "pay";
        String orderType = "other";
        if ((totalMoney.doubleValue()+shipfee - bill.getPaymentAmount().doubleValue() )*100<1000000){
            return ResponseEntity.badRequest().body("Không thể thanh toán số tiền nhỏ hơn 10,000");
        }
        if (bill.getType()==0){
            return ResponseEntity.badRequest().body("Không thể thanh toán đơn đã hủy");
        }
        if (bill.getType()>=4){
            return ResponseEntity.badRequest().body("Không thể thanh toán đơn đã chờ giao hàng");
        }
        long amount = (long)((totalMoney.doubleValue()+shipfee - bill.getPaymentAmount().doubleValue() )*100);
        String bankCode = "NCB";

        String vnp_TxnRef = ConfigVNPay.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = ConfigVNPay.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", df.format(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

//        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");
        String vnp_ReturnUrl = ConfigVNPay.vnp_ReturnUrl+"?billId="+billID;
        if (admin!=null){
            vnp_ReturnUrl+="&admin=x";
        }

        vnp_Params.put("vnp_ReturnUrl",vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = ConfigVNPay.hmacSHA512(ConfigVNPay.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = ConfigVNPay.vnp_PayUrl + "?" + queryUrl;
        return ResponseEntity.ok(new MessageResponse(paymentUrl)) ;



	}

}
