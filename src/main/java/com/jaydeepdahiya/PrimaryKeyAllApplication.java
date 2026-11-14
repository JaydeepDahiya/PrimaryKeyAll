// Author: Jaydeep Dahiya
package com.jaydeepdahiya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class PrimaryKeyAllApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrimaryKeyAllApplication.class, args);
    }
}
