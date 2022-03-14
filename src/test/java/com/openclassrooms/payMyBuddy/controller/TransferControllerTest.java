package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.ConnectionService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransferController.class)
public class TransferControllerTest {
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
    public void redirectTransferPageTestShouldRedirectToLoginPage() throws Exception {
        when(userService.currentUser()).thenReturn(null);
        mockMvc.perform(get("/transfer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}
