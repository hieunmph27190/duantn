package com.fpt.duantn;

import com.fpt.duantn.repository.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DuantnApplication {
    public static void main(String[] args) {
        SpringApplication.run(DuantnApplication.class, args);
        Test.test();
    }

}
