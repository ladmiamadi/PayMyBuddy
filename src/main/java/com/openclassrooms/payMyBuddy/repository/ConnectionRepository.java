package com.openclassrooms.payMyBuddy.repository;

import com.openclassrooms.payMyBuddy.model.Connection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends CrudRepository<Connection, Integer> {
}
