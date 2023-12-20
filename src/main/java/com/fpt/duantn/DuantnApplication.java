package com.fpt.duantn;

import com.fpt.duantn.repository.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DuantnApplication {
    public static void main(String[] args) {
        SpringApplication.run(DuantnApplication.class, args);
//       try {
//           Thread.sleep(5000);
//       }catch (Exception exception){
//
//       }
//        Test.test();
    }

}
