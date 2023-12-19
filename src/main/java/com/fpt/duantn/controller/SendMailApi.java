/*
 * (C) Copyright 2022. All Rights Reserved.
 *
 * @author DongTHD
 * @date Mar 10, 2022
*/
package com.fpt.duantn.controller;

import com.fpt.duantn.domain.Customer;
import com.fpt.duantn.service.CustomerService;
import com.fpt.duantn.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/send-mail")
public class SendMailApi {

	@Autowired
	SendMailService sendMail;

	@Autowired
	CustomerService customerService;

	@PostMapping("/otp")
	public ResponseEntity<Integer> sendOpt(@RequestBody String email) {
		int random_otp = (int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000);
		if (customerService.existsByEmail(email)) {
			return ResponseEntity.notFound().build();
		}
		sendMailOtp(email, random_otp, "Xác nhận tài khoản!");
		return ResponseEntity.ok(random_otp);
	}


	// sendmail
	public void sendMailOtp(String email, int password, String title) {
		String body = "<div>\r\n" + "<h3>Mật khẩu của bạn là: <span style=\"color:red; font-weight: bold;\">"
				+ password + "</span></h3>\r\n" + "    </div>";
		sendMail.queue(email, title, body);
	}

}