package com.openclassrooms.payMyBuddy.repository;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Page<Transaction> findTransactionsByUserAndType(User user, String type, Pageable pageable);

    Page<Transaction> findTransactionsByUserAndPayedUserEquals(User user, User payedUser, Pageable pageable);
}
