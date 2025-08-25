package ru.practicum.ewm.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ru.practicum.ewm")
public class EwmServerMain {
    public static void main(String[] args) {
        SpringApplication.run(EwmServerMain.class, args);
    }
}
