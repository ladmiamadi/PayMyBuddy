package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.TransactionRepository;
import com.openclassrooms.payMyBuddy.service.ConnectionService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.math.BigDecimal;
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

    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/")
    public String redirectHome() {
        return "redirect:/home";
    }

    @GetMapping("/home/{pageNumber}")
    public String getOnePage (Model model, @PathVariable("pageNumber") int currentPage) {
        User currentUser = userService.getUserById(1).get();
        Page<Transaction> page = transactionService.findPage(currentUser, "payment","", currentPage);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<Transaction> transactions = page.getContent();
        // send data to view
        List<User> addedUsers = currentUser.getAddedUsers();
        Transaction transaction = new Transaction();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("transactions", transactions);
        model.addAttribute("transaction", transaction);
        model.addAttribute("addedUsers", addedUsers);

        return "home";
    }

    @GetMapping("/home")
    public String getAllPages(Model model){
        return getOnePage(model, 1);
    }

    @PostMapping("/home")
    public String handleForm(@Valid Transaction transaction,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes, Model model) {
        User currentUser = userService.getUserById(1).get();

        if (bindingResult.hasErrors()) {
            Page<Transaction> page = transactionService.findPage(currentUser, "payment","", 1);
            int totalPages = page.getTotalPages();
            long totalItems = page.getTotalElements();

            model.addAttribute("currentPage", 1);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalItems", totalItems);
            model.addAttribute("transactions", page.getContent());
            model.addAttribute("transaction", transaction);
            model.addAttribute("addedUsers", currentUser.getAddedUsers());

            return "home";
        }

        if(transaction.getPayedUser() == null) {
            redirectAttributes.addFlashAttribute("error", "Select a valid connection!");
            return "redirect:/home";
        }

        if (currentUser.getBalance().subtract(transaction.getAmount()).compareTo(BigDecimal.valueOf(0)) < 0) {
            redirectAttributes.addFlashAttribute("error", "Your balance is insufficient!");
        } else {
            transactionService.createNewTransaction(currentUser, transaction.getAmount(), userService.getUserById(transaction.getPayedUser().getId()).get(), "payment");
            // update user balance
            currentUser.setBalance(currentUser.getBalance().subtract(transaction.getAmount()));
            userService.updateUser(currentUser);

            redirectAttributes.addFlashAttribute("success", "successfully payed");
        }
        return "redirect:/home";
    }


}
