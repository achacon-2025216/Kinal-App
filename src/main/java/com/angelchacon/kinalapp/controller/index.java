package com.angelchacon.kinalapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class index {
    
    @GetMapping("/")
    public String index() {
        return "index"; // Esto busca templates/index.html
    }
}