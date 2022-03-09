package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.model.Connection;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {
    @Autowired
    private ConnectionRepository connectionRepository;

    public void saveConnection (Connection connection) {
        connectionRepository.save(connection);
    }

    public void createNewConnection(User currentUser, User user) {
        Connection connection = new Connection();
        connection.setUserId(currentUser.getId());
        connection.setAddedUserId(user.getId());

        connection.setDateAdded(HelperService.formattingNewDate());
        saveConnection(connection);
    }
}
