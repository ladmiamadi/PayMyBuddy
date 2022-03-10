package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.TransactionsExceptions;
import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserService userService;

    public Iterable<Transaction> getTransactions () {
        return transactionRepository.findAll();
    }

    public Transaction addTransaction (Transaction transaction) {

        return transactionRepository.save(transaction);
    }

    public Page<Transaction> findHomePage(User user, String type, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber -1,5, Sort.by("transactionDate").descending());
        return transactionRepository.findTransactionsByUserAndType(user, type, pageable);
    }

    public Page<Transaction> findTransferPage(User user, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber -1,5, Sort.by("transactionDate").descending());
        return transactionRepository.findTransactionsByUserAndPayedUserEquals(user, null, pageable);
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

    @Transactional(rollbackFor = TransactionsExceptions.class)
    public void payUser(User currentUser, Transaction transaction) throws TransactionsExceptions{
        User payedUser = transaction.getPayedUser();
        if (payedUser == null) {
           throw new TransactionsExceptions("Select a valid connection!");}

        if (HelperService.calculateBalance(currentUser, transaction) < 0) {
            throw new TransactionsExceptions("You have "+ currentUser.getBalance()+ " € in your account. Your balance is insufficient!");
            }
        createNewTransaction(currentUser, transaction.getAmount(), payedUser, "payment");
        currentUser.setBalance(currentUser.getBalance().subtract(transaction.getAmount()));
        payedUser.setBalance(payedUser.getBalance().add(transaction.getAmount()));
        userService.updateUser(payedUser);
        userService.updateUser(currentUser);
    }

    @Transactional(rollbackFor = TransactionsExceptions.class)
    public void transferMoney(User currentUser, Transaction transaction) throws TransactionsExceptions{
        String type = transaction.getType();

        if(Objects.equals(type, "transfer")) {
            if (HelperService.calculateBalance(currentUser, transaction) < 0) {
                throw new TransactionsExceptions("Your balance is insufficient!");
            }

            currentUser.setBalance(currentUser.getBalance().subtract(transaction.getAmount()));
            currentUser.setBankAccountBalance(currentUser.getBankAccountBalance().add(transaction.getAmount()));
        } else if(Objects.equals(type, "receive")){
            if (currentUser.getBankAccountBalance().subtract(transaction.getAmount()).compareTo(BigDecimal.valueOf(0)) < 0) {
                throw new TransactionsExceptions("Your balance is insufficient!");
            }

            currentUser.setBalance(currentUser.getBalance().add(transaction.getAmount()));
            currentUser.setBankAccountBalance(currentUser.getBankAccountBalance().subtract(transaction.getAmount()));
        } else {
            throw new TransactionsExceptions("Invalid operation");
        }

        createNewTransaction(currentUser, transaction.getAmount(), null, type);
        userService.updateUser(currentUser);
    }
}
