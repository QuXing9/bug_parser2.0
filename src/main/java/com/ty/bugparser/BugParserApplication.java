package com.ty.bugparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BugParserApplication {

    public static void main(String[] args) {
        SpringApplication.run(BugParserApplication.class, args);
    }

}
