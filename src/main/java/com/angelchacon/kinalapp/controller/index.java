package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.Usuario; // Asegúrate de que esta ruta a tu entidad sea correcta
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class index {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("successmessage", "Bienvenido al Sistema Kinal-App"); // Mensaje de bienvenida
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        // Busca el archivo en templates/html/login.html
        return "html/login";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        // Agregamos el objeto para que el formulario th:object="${usuarioNuevo}" no de error
        model.addAttribute("usuarioNuevo", new Usuario());

        // Busca el archivo en templates/html/registro.html
        return "html/registro";
    }
}