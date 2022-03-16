package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.TransactionRepository;
import com.openclassrooms.payMyBuddy.service.ConnectionService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.sql.DataSource;
import java.util.List;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    ConnectionService connectionService;

    @MockBean
    DataSource dataSource;

    @MockBean
    TransactionService transactionService;


    @Test
    public void redirectHomeTestShouldRedirectToHomeView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home"));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void getOnePageTestShouldReturnHomePage() throws Exception {
        Transaction transaction = new Transaction();

        User currentUser = new User();
        currentUser.setEmail("ladmia.madi@gmail.com");
        currentUser.setId(1);
        currentUser.setFirstName("Ladmia");
        transaction.setPayedUser(currentUser);
        Iterable<Transaction> transactions = new PageImpl<Transaction>(List.of(transaction)).getContent();

        when(userService.currentUser()).thenReturn(currentUser);
        when(transactionService.getTransactions()).thenReturn(transactions);

        when(transactionService.findHomePage(currentUser, "payment", 1)).thenReturn(new PageImpl<>(List.of(transaction)));

        mockMvc.perform(get("/home/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))

                .andExpect(model().attributeExists("transactions"));
    }

    @Test
    public void getOnePageTestFailShouldRedirectToLoginPage() throws Exception {
        when(userService.currentUser()).thenReturn(null);

        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void handleFormTest() throws Exception {
        Transaction transaction = new Transaction();

        User currentUser = new User();
        currentUser.setEmail("ladmia.madi@gmail.com");
        currentUser.setId(1);
        currentUser.setFirstName("Ladmia");
        transaction.setPayedUser(currentUser);

        Iterable<Transaction> transactions = new PageImpl<Transaction>(List.of(transaction)).getContent();

        when(userService.currentUser()).thenReturn(currentUser);
        when(transactionService.getTransactions()).thenReturn(transactions);

        when(transactionService.findHomePage(currentUser, "payment", 1)).thenReturn(new PageImpl<>(List.of(transaction)));

        mockMvc.perform(post("/home"))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());
    }

}
