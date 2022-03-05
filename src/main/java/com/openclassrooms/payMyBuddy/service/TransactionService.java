package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    public Iterable<Transaction> getTransactions () {
        return transactionRepository.findAll();
    }

    public Transaction addTransaction (Transaction transaction) {

        return transactionRepository.save(transaction);
    }

    public void deleteTransaction (Transaction transaction) {
        transactionRepository.delete(transaction);
    }

    public void deleteTransactionById (Integer id) {
        transactionRepository.deleteById(id);
    }

    public void createNewTransaction(User user, BigDecimal amount, User payedUser, String type) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setUser(user);
        transaction.setPayedUser(payedUser);
        transaction.setType(type);
        transaction.setTransactionDate(HelperService.formattingNewDate());

        transactionRepository.save(transaction);
    }
}
