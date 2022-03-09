package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelperService {
    public static String formattingNewDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static BigDecimal randomBalance() {
        return BigDecimal.valueOf(Math.random()*(1000-100+1)+100);
    }

    public static int calculateBalance (User user, Transaction transaction) {
        return user.getBalance().subtract(transaction.getAmount()).compareTo(BigDecimal.valueOf(0));
    }
}
