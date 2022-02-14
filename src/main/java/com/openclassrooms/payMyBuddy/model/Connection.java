package com.openclassrooms.payMyBuddy.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ajouter_liste")
@Getter
@Setter
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_connection", nullable = false)
    private Integer connectionId;

    /*@ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="id_utilisateur")
    private User user;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="id_utilisateur_ajoute")
    private User addedUser;*/

    @Column(name = "id_utilisateur")
    private Integer userId;

    @Column(name = "id_utilisateur_ajoute")
    private Integer addedUserId;

    @Column(name = "date_ajout")
    private String dateAdded;
}
