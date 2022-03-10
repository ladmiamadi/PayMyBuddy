package com.openclassrooms.payMyBuddy.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="compte_utilisateur")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "email", unique = true)
    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Invalid email format")
    private String email;

    @Column(name = "mot_de_passe")
    @NotEmpty(message = "Password can not be empty")
    private String password;

    @Column(name = "prenom")
    @NotEmpty(message = "First Name can not be empty")
    private String firstName;

    @Column(name = "nom")
    @NotEmpty(message = "Last Name can not be empty")
    private String lastName;

    @Column(name = "solde")
    private BigDecimal balance;

    @Column(name = "compte_bancaire")
    @NotEmpty(message = "Bank Account can not be empty")
    private String bankAccount;

    @Column(name = "solde_compte_bancaire")
    private BigDecimal bankAccountBalance;

    @Column(name = "date_inscription")
    private String registrationDate;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<Transaction> transactions = new ArrayList<>();

    @OneToMany(
            mappedBy = "payedUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
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
