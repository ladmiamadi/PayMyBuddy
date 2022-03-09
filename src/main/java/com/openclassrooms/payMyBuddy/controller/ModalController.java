package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.model.Connection;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.ConnectionService;
import com.openclassrooms.payMyBuddy.service.HelperService;
import com.openclassrooms.payMyBuddy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ModalController {
    @Autowired
    private UserService userService;

    @Autowired
    private ConnectionService connectionService;

    @PostMapping("/modal")
    @ResponseBody
    public ResponseEntity<?> newConnection (@RequestBody String email, Errors errors) {
        Map<String, String> result = new HashMap<>();

        if(errors.hasErrors()) {
            result.put("message", errors.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);
        }
        User currentUser = userService.currentUser();
        email = email.replace("\"", "");
        Optional<User> user = userService.getUserByEmail(email);

        if(user.isPresent()) {
            if(currentUser.getAddedUsers().contains(user)) {
                result.put("error", "Connection already exist in your list");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

            connectionService.createNewConnection(currentUser, user.get());
            result.put("success", "Connection was successfully added");
        } else {
            result.put("error", "Email not found in our database");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
