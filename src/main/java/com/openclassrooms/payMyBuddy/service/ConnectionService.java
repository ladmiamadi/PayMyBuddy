package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.model.Connection;
import com.openclassrooms.payMyBuddy.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {
    @Autowired
    private ConnectionRepository connectionRepository;

    public Connection addConnection (Connection connection) {
        return connectionRepository.save(connection);
    }
}
