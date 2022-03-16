package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.exceptions.TransactionsExceptions;
import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
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


    @GetMapping("/")
    public String redirectHome() {
        return "redirect:/home";
    }

    @GetMapping("/home/{pageNumber}")
    public String getOnePage (Model model, @PathVariable("pageNumber") int currentPage) {
        if(userService.currentUser() == null) {
            log.info("Unknown user, you had been redirected to login page");
            return "redirect:/login";
        } else {
            User currentUser = userService.currentUser();
            Page<Transaction> page = transactionService.findHomePage(currentUser, "payment", currentPage);
            int totalPages = page.getTotalPages();
            long totalItems = page.getTotalElements();
            List<Transaction> transactions = page.getContent();
            List<User> addedUsers = currentUser.getAddedUsers();
            Transaction transaction = new Transaction();

            //Send data to view
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalItems", totalItems);
            model.addAttribute("transactions", transactions);
            model.addAttribute("transaction", transaction);
            model.addAttribute("addedUsers", addedUsers);

            log.info("Displaying home page");
            return "home";
        }
    }

    @GetMapping("/home")
    public String getAllPages(Model model){
        return getOnePage(model, 1);
    }

    @PostMapping("/home")
    public String handleForm(@Valid Transaction transaction,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes, Model model) {
        if(userService.currentUser() == null) {
            log.info("Unknown user, you had been redirected to login page");
            return "redirect:/login";
        } else {
            User currentUser = userService.currentUser();

            //capture errors from fields in the payment form
            if (bindingResult.hasErrors()) {
                log.error("There are errors in the form, error ="+ bindingResult.getAllErrors());

                Page<Transaction> page = transactionService.findHomePage(currentUser, "payment", 1);
                int totalPages = page.getTotalPages();
                long totalItems = page.getTotalElements();

                //display form errors in home page
                model.addAttribute("currentPage", 1);
                model.addAttribute("totalPages", totalPages);
                model.addAttribute("totalItems", totalItems);
                model.addAttribute("transactions", page.getContent());
                model.addAttribute("transaction", transaction);
                model.addAttribute("addedUsers", currentUser.getAddedUsers());

                return "home";
            }
                try {
                    //Transactional method
                    log.debug("Creating Transaction: "+ transaction);

                    transactionService.payUser(currentUser, transaction);
                    redirectAttributes.addFlashAttribute("success", "successfully payed");
                } catch (TransactionsExceptions e) {
                    redirectAttributes.addFlashAttribute("error", e.getMessage());

                    log.error("Enable to create transaction, error="+ e.getMessage());
                    return "redirect:/home";
                }
            return "redirect:/home";
        }
    }
}
