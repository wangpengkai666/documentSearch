package com.example.documentseach;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(App.class);
        springApplicationBuilder.application().setAdditionalProfiles("dev");
        springApplicationBuilder.run();
    }
}
