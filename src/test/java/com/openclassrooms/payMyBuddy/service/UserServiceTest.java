package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @MockBean
    UserRepository userRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Test
    @WithMockUser
    public void testRegister() {
        User user = new User();
        user.setPassword("123");

        when(userRepository.save(user)).thenReturn(user);

        assertThat(userService.registerUser(user)).isEqualTo(user);

    }

    @Test
    public void testCheckIfUserExists() {
        User user = new User();
        user.setEmail("ladmia.madi@gmail.com");

        when(userRepository.findByEmail("ladmia.madi@gmail.com")).thenReturn(Optional.of(user));

        assertThat(userService.checkIfUserExist("ladmia.madi@gmail.com")).isTrue();
    }

    @Test
    public void testEncodePassword() {
        User user = new User();
        user.setPassword(passwordEncoder.encode("123"));

        String result = user.getPassword();

        user.setPassword("123");
        userService.encodePassword(user);
        assertThat(result).isEqualTo(user.getPassword());
    }
}
