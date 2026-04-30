package com.angelchacon.kinalapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class index {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("successmessage", "Bienvenido al Sistema Kinal-App");
        // Según tu imagen, index.html está fuera de la carpeta html
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "html/login";
    }
}