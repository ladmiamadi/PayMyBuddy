package com.openclassrooms.payMyBuddy.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transaction")
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction", nullable = false)
    private Integer transactionId;

    @Column(name = "montant")
    private BigDecimal amount;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "date_transaction")
    private String transactionDate;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "id_utilisateur")
    private User user;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "id_utilisateur_paye")
    private User payedUser;
}
