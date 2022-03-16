package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.exceptions.TransactionsExceptions;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@Slf4j
public class ProfileController {
    @Autowired
    UserService userService;

    @GetMapping("/profile")
    public String getProfile(Model model) {
        User currentUser = userService.currentUser();
        if(currentUser == null) {
            log.info("Unknown user, you had been redirected to login page");
            return "redirect:/login";
        } else {
            List<User> connections = userService.currentUser().getAddedUsers();

            //display user information and connections
            model.addAttribute("user", currentUser);
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
                log.error("Enable to delete connection , error= "+ e.getMessage());
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
            log.info("Connection with id "+ id + " was successfully deleted");
            return "redirect:/profile";
    }

    @GetMapping("/contact")
    public String getContactPage() {
        return "contact";
    }
}
