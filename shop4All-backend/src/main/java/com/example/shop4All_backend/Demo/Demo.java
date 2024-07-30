package com.example.shop4All_backend.Demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Demo {

    @GetMapping({"/"})
    public String demo() {
        return "Hello World";
    }

    @GetMapping({"/shop4All"})
    public String shop4AllDemo() {
        return "Shop4All Demo";
    }

    @GetMapping({"/shop4All/product/1"})
    public String product1Demo() {
        return "Product 1";
    }

    @GetMapping({"/shop4All/product/2"})
    public String product2Demo() {
        return "Product 2";
    }
}
