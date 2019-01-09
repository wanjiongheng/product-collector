package com.netopstec.productcollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ProductCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductCollectorApplication.class, args);
    }

}

