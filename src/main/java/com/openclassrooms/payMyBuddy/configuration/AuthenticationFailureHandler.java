package com.openclassrooms.payMyBuddy.configuration;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws ServletException, IOException {
        super.onAuthenticationFailure(request, response, exception);
        if (exception.getClass().isAssignableFrom(UsernameNotFoundException.class)) {
            response.sendRedirect("Invalid email or password, try to correct informations or '<a>Sign up<a/> on our application");
        }
    }
}
