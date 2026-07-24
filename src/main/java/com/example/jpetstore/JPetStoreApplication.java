package com.example.jpetstore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.jpetstore.mapper")
public class JPetStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(JPetStoreApplication.class, args);
    }
}
