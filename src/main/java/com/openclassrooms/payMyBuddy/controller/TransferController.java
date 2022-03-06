package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Controller
public class TransferController {
    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;

    @GetMapping("/transfer/{pageNumber}")
    public String getOnePage (Model model, @PathVariable("pageNumber") int currentPage) {
        User currentUser = userService.getUserById(1).get();
        Page<Transaction> page = transactionService.findPage(currentUser, "transfer", "receive", currentPage);

        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<Transaction> transactions = page.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("transactions", transactions);
        model.addAttribute("user", currentUser);

        return "transfer";
    }

    @GetMapping("/transfer")
    public String getAllPages(Model model){
        return getOnePage(model, 1);
    }

    @PostMapping("/transfer")
    public String transferMoney(@RequestParam("amount") BigDecimal amount,
                                @RequestParam("type") String type, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getUserById(1).get();

        //send money to the bank account
        if(Objects.equals(type, "transfer")) {
            if(currentUser.getBalance().subtract(amount).compareTo(BigDecimal.valueOf(0)) < 0) {
                redirectAttributes.addFlashAttribute("error", "Your balance is insufficient!");
                return "redirect:/transfer";
            } else {
                currentUser.setBalance(currentUser.getBalance().subtract(amount));
                currentUser.setBankAccountBalance(currentUser.getBankAccountBalance().add(amount));
                userService.updateUser(currentUser);
                //Create transaction
                transactionService.createNewTransaction(currentUser, amount, null, "transfer");

                redirectAttributes.addFlashAttribute("success", "money successfully transferred");
            }
        } else if(Objects.equals(type, "receive")) {
            if(currentUser.getBankAccountBalance().subtract(amount).compareTo(BigDecimal.valueOf(0)) < 0) {
                redirectAttributes.addFlashAttribute("error", "Your balance is insufficient!");
                return "redirect:/transfer";
            } else {
                currentUser.setBalance(currentUser.getBalance().add(amount));
                currentUser.setBankAccountBalance(currentUser.getBankAccountBalance().subtract(amount));
                userService.updateUser(currentUser);

                transactionService.createNewTransaction(currentUser, amount, null, "receive");
                redirectAttributes.addFlashAttribute("success", "money successfully received");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid operation");
        }

        return "redirect:/transfer";
    }
}
