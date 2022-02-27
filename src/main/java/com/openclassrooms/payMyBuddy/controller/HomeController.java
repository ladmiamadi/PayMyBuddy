package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.ConnectionService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        List<Transaction> transactions = userService.getUserById(1).get().getTransactions();
        List<User> addedUsers = userService.getUserById(1).get().getAddedUsers();

        model.addAttribute("transactions", transactions);
        model.addAttribute("addedUsers", addedUsers);

        return "home";
    }

    @PostMapping("/home/payment")
    public RedirectView handleForm(@RequestParam("connection") Integer connection,
                                   @RequestParam("amount") Integer amount, RedirectAttributes redirectAttributes) {

        Transaction transaction = new Transaction();
        transaction.setUser(userService.getUserById(1).get());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        transaction.setTransactionDate(dateFormat.format(date));
        transaction.setAmount(BigDecimal.valueOf(amount));
        transaction.setType("payment");

        transaction.setPayedUser(userService.getUserById(connection).get());

        transactionService.addTransaction(transaction);

        redirectAttributes.addFlashAttribute("success", "successfully payed");
        return new RedirectView("/home");
    }


}
