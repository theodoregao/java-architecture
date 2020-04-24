package com.sg.shopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SsoController {

    @GetMapping("/login")
    public String login(String returnUrl, Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        model.addAttribute("returnUrl", returnUrl);
        return "login"; // this will redirect to login.html resource file.
    }

}
