package com.openclassrooms.payMyBuddy.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transaction")
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction", nullable = false)
    private Integer transactionId;

    @Min(value = 1, message = "The amount must be greater than 0€")
    @NotNull(message = "Amount can't be null")
    @Digits(integer=4, fraction=2, message = "Invalid amount")
    @Column(name = "montant")
    private BigDecimal amount;

    @NotNull
    @Pattern(regexp="^[A-Za-z]*$",message = "Select a valid type")
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
