package com.example.stripespoc.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class UiLoader {

    @GetMapping(path = { "/", "/index.html", "/customer/**", "/charge/**", "/connectedAccount"})
    public Mono<Rendering> loadAngularApp() {
        return Mono.just(Rendering.view("index")
                                  .build());
    }
}
