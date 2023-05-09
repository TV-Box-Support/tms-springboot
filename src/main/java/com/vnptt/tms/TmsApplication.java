package com.vnptt.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class })
public class TmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TmsApplication.class, args);
    }

}
