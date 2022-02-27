package com.openclassrooms.payMyBuddy.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ajouter_liste")
@Getter
@Setter
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_connection", nullable = false)
    private Integer connectionId;


    @Column(name = "id_utilisateur")
    private Integer userId;

    @Column(name = "id_utilisateur_ajoute")
    private Integer addedUserId;

    @Column(name = "date_ajout")
    private String dateAdded;
}
