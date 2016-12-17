package com.example;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

    @GetMapping
    public String home(Principal principal) {
        String username = principal.getName();
        return String.format("Hello, %s!", username);
    }
}
