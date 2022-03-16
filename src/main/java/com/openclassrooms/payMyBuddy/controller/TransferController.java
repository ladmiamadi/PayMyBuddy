package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.exceptions.TransactionsExceptions;
import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class TransferController {
    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;

    @GetMapping("/transfer/{pageNumber}")
    public String getOnePage (Model model, @PathVariable("pageNumber") int currentPage) {
        if(userService.currentUser() == null) {
            log.info("Unknown user, you had been redirected to login page");
            return "redirect:/login";
        } else {
            User currentUser = userService.currentUser();
            Page<Transaction> page = transactionService.findTransferPage(currentUser, currentPage);

            int totalPages = page.getTotalPages();
            long totalItems = page.getTotalElements();
            List<Transaction> transactions = page.getContent();
            Transaction transaction = new Transaction();

            //send data to view
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalItems", totalItems);
            model.addAttribute("transactions", transactions);
            model.addAttribute("transaction", transaction);
            model.addAttribute("user", currentUser);

            return "transfer";
        }
    }

    @GetMapping("/transfer")
    public String getAllPages(Model model){
        return getOnePage(model, 1);
    }

    @PostMapping("/transfer")
    public String transferMoney(@Valid Transaction transaction, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes, Model model) {
        if(userService.currentUser() == null) {
            log.info("Unknown user, you had been redirected to login page");
            return "redirect:/login";
        } else {
            User currentUser = userService.currentUser();

            if (bindingResult.hasErrors()) {
                log.error("There are errors in transfer form, errors= "+ bindingResult.getAllErrors());

                Page<Transaction> page = transactionService.findTransferPage(currentUser, 1);
                int totalPages = page.getTotalPages();
                long totalItems = page.getTotalElements();
                List<Transaction> transactions = page.getContent();

                model.addAttribute("currentPage", 1);
                model.addAttribute("totalPages", totalPages);
                model.addAttribute("totalItems", totalItems);
                model.addAttribute("transactions", transactions);
                model.addAttribute("transaction", transaction);
                model.addAttribute("user", currentUser);

                return "transfer";
            }
                try {
                    transactionService.transferMoney(currentUser, transaction);
                    log.info("money successfully received");
                    redirectAttributes.addFlashAttribute("success", "money successfully received");
                } catch (TransactionsExceptions e) {
                    log.error("Enable to transfer money because error= "+ e.getMessage());
                    redirectAttributes.addFlashAttribute("error", e.getMessage());
                }

            return "redirect:/transfer";
        }
    }
}
