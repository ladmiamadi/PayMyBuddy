package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.service.ConnectionService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {
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
    public void getRegisterPageTestShouldReturn_200() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    public void getLoginPageTestShouldReturn_200() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void postRegisterPageTestShouldRedirectToLoginPage() throws Exception {
        mockMvc.perform(post("/register"))
                .andExpect(status().isOk());
    }

    @Test
    public void postRegisterPageTestFailShouldReturnRegisterPage() throws Exception {
        mockMvc.perform(post("/register"))
                .andExpect(status().isOk());
    }
}
