package ru.practicum.ewm.statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ru.practicum.ewm")
public class EwmStatsServer {

    public static void main(String[] args) {
        SpringApplication.run(EwmStatsServer.class, args);
    }
}
