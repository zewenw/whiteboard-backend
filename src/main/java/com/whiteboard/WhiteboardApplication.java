package com.whiteboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class WhiteboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhiteboardApplication.class, args);
    }

}
