package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public Mono<String> hello() {
        // just() 方法可以指定序列中包含的全部元素
        return Mono.just("Welcome to reactive world ~");
    }
}