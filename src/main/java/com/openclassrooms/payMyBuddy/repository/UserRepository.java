package com.openclassrooms.payMyBuddy.repository;

import com.openclassrooms.payMyBuddy.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findAddedUsersByEmail(String email);

    @Modifying
    @Query(value = "delete from ajouter_liste where ajouter_liste.id_utilisateur_ajoute= ?",
            nativeQuery = true)
   void deleteAddedUserById(Integer id);


}
