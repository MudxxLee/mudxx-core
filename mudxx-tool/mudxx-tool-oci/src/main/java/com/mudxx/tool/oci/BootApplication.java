package com.mudxx.tool.oci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author laiw
 * @date 2023/4/3 11:45
 */
@SpringBootApplication(scanBasePackages = "com.mudxx")
public class BootApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }
}
