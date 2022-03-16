package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j
@Controller
public class LoginController {
    @Autowired
    UserService userService;

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
    public String createAccount(@Valid User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            log.error("There are errors n register form: " + bindingResult.getAllErrors());
            model.addAttribute("user", user);
            return "register";
        }

        //Check if email is unique
        if(userService.checkIfUserExist(user.getEmail())) {
            log.error("Email already exists: " + user.getEmail() );

            bindingResult.addError(new FieldError("email", "email", "Email already exists"));
            model.addAttribute("user", user);
            return "register";
        } else {
            userService.registerUser(user);
        }

        log.debug(user + " had been created in database");
        return "redirect:/login";
    }
}

