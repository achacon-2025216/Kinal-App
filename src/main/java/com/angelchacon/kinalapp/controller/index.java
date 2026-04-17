package com.angelchacon.kinalapp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class index {

    @GetMapping("/")
    public String home(HttpSession session) {
        if (session.getAttribute("usuarioActivo") == null) {
            return "redirect:/login";
        }
        return "index";
    }


}