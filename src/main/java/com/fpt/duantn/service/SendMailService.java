/*
 * (C) Copyright 2022. All Rights Reserved.
 *
 * @author DongTHD
 * @date Mar 10, 2022
*/
package com.fpt.duantn.service;

import com.fpt.duantn.dto.MailInfo;
import jakarta.mail.MessagingException;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public interface SendMailService {

	void run();

	void queue(String to, String subject, String body);

	void queue(MailInfo mail);

	void send(MailInfo mail) throws MessagingException, IOException;

}
