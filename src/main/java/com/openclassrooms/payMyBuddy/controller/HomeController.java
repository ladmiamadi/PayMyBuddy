package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    @Autowired
    UserService userService;

    @GetMapping("/home")
    public String getTransactions (Model model) {
        List<Transaction> transactions = userService.getUserById(1).get().getTransactions();

        model.addAttribute("transactions", transactions);

        return "home";
    }
}
