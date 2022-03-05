package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.ConnectionService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Log4j2
@Controller
public class HomeController {
    @Autowired
    UserService userService;

    @Autowired
    ConnectionService connectionService;

    @Autowired
    TransactionService transactionService;

    @GetMapping("/home")
    public String getTransactions (Model model) {
        User currentUser = userService.getUserById(1).get();
        List<Transaction> transactions = currentUser.getTransactions();
        List<User> addedUsers = currentUser.getAddedUsers();

        model.addAttribute("transactions", transactions);
        model.addAttribute("addedUsers", addedUsers);

        return "home";
    }

    @PostMapping("/home")
    public String handleForm(@RequestParam("connection") Integer connection,
                             @RequestParam(value = "amount") BigDecimal amount, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getUserById(1).get();

        if (currentUser.getBalance().subtract(amount).compareTo(BigDecimal.valueOf(0)) < 0) {
            redirectAttributes.addFlashAttribute("error", "Your balance is insufficient!");
        }else if(connection == 0){
            redirectAttributes.addFlashAttribute("error", "Select a connection first");
        }else {
            transactionService.createNewTransaction(currentUser, amount, userService.getUserById(connection).get(), "payment");
            // update user balance
            currentUser.setBalance(currentUser.getBalance().subtract(amount));
            userService.updateUser(currentUser);

            redirectAttributes.addFlashAttribute("success", "successfully payed");
        }
        return "redirect:/home";
    }


}
