package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String signIn () {
        return "login";
    }

    @GetMapping("/register")
    public String signUp (Model model) {
        User user = new User();

        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String createAccount(@Valid User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "register";
        }
        return "login";
    }
}

