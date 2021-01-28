package ru.subbotinkv.widgets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class WidgetsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WidgetsApplication.class, args);
    }

}
