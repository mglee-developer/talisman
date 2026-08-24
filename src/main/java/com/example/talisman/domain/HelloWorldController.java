package com.example.talisman.domain;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloWorldController {
    @GetMapping
    public String HelloWorld() {
        return "Hello World!";
    }

    @PostMapping
    public String HelloWorld(@Parameter String sayHello) {
        return sayHello;
    }
}
