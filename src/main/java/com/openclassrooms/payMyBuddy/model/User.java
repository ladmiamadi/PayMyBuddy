package com.openclassrooms.payMyBuddy.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="compte_utilisateur")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "mot_de_passe")
    private String password;

    @Column(name = "prenom")
    private String firstName;

    @Column(name = "nom")
    private String lastName;

    @Column(name = "solde")
    private BigDecimal balance;

    @Column(name = "compte_bancaire")
    private String bankAccount;

    @Column(name = "solde_compte_bancaire")
    private BigDecimal bankAccountBalance;

    @Column(name = "date_inscription")
    private String registrationDate;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "id_utilisateur")
    List<Transaction> transactions = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "id_utilisateur_paye")
    List<Transaction> paymentTransactions = new ArrayList<>();

    @ManyToMany (
           fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.ALL,
            }
    )
    @JoinTable(name="ajouter_liste",
            joinColumns=@JoinColumn(name="id_utilisateur"),
            inverseJoinColumns=@JoinColumn(name="id_utilisateur_ajoute")
    )
    private List<User> addedUsers = new ArrayList<>();
}