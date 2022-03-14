package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.exceptions.TransactionsExceptions;
import com.openclassrooms.payMyBuddy.model.Connection;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    UserService userService;

    @GetMapping("/profile")
    public String getProfile(Model model) {
        if(userService.currentUser() == null) {
            return "redirect:/login";
        } else {
            List<User> connections = userService.currentUser().getAddedUsers();
            model.addAttribute("connections", connections);
            return "profile";
        }
    }

    @GetMapping("/delete")
    public String delete(Integer id, RedirectAttributes redirectAttributes) {
            try {
                userService.deleteAddedUser(id, userService.currentUser());
                redirectAttributes.addFlashAttribute("success", "Connection was successfully deleted");
            } catch (TransactionsExceptions e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
            return "redirect:/profile";
    }


    @GetMapping("/contact")
    public String getContactPage() {
        return "contact";
    }
}
