package com.fpt.duantn.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Test {

   public static void test(){
       PasswordEncoder encoder = new BCryptPasswordEncoder();
       System.out.println(encoder.encode("1"));
   }
}
