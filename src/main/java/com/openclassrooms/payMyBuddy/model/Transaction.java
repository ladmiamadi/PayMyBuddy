package com.openclassrooms.payMyBuddy.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "transaction")
@Data
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
