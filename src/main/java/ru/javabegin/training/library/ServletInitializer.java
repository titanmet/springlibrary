package ru.javabegin.training.library;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.javabegin.training.library"})
public class ServletInitializer extends SpringBootServletInitializer {

}

