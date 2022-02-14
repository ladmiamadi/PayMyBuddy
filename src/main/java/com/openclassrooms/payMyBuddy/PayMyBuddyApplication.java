package com.openclassrooms.payMyBuddy;

import com.openclassrooms.payMyBuddy.model.Connection;
import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.ConnectionService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class PayMyBuddyApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Autowired
	private ConnectionService connectionService;

	@Autowired
	private TransactionService transactionService;

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		/*User userToAdd = new User();

		userToAdd.setEmail("lea.anais@gmail.com");
		userToAdd.setFirstName("Anais");
		userToAdd.setLastName("Madi");
		userToAdd.setBankAccount("1985FF");
		userToAdd.setBalance(BigDecimal.valueOf(100));

		userToAdd.setBankAccountBalance(BigDecimal.valueOf(1000));
		userToAdd.setPassword("anais2015");

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		userToAdd.setRegistrationDate(dateFormat.format(date));

		System.out.println(userToAdd.getRegistrationDate());

		userService.addUser(userToAdd);*/

		//Connection connection = new Connection();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		//connection.setDateAdded(dateFormat.format(date));
		//connection.setUserId(1);
		//connection.setAddedUserId(4);

		//connectionService.addConnection(connection);
		Transaction transaction = new Transaction();

		transaction.setTransactionDate(date);
		transaction.setAmount(BigDecimal.valueOf(100));
		transaction.setType("Transfert");
		transaction.setUserId(1);

		transactionService.addTransaction(transaction);

		Optional<User> user = userService.getUserById(1);

		//System.out.println(user.get().getAddedUsers());
		System.out.println(user.get().getTransactions());
	}
}
