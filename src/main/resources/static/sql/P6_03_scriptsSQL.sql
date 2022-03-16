CREATE DATABASE `paymybuddy`;
USE `paymybuddy`;

CREATE TABLE `compte_utilisateur` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `mot_de_passe` varchar(100) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `solde` decimal(5,2) NOT NULL,
  `compte_bancaire` varchar(100) NOT NULL,
  `solde_compte_bancaire` decimal(5,2) NOT NULL,
  `date_inscription` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
);

CREATE TABLE `ajouter_liste` (
  `id_connection` int NOT NULL AUTO_INCREMENT,
  `id_utilisateur` int NOT NULL,
  `id_utilisateur_ajoute` int NOT NULL,
  `date_ajout` datetime NOT NULL,
  PRIMARY KEY (`id_connection`),
  KEY `id_utilisateur` (`id_utilisateur`),
  KEY `id_utilisateur_ajoute` (`id_utilisateur_ajoute`),
  CONSTRAINT `ajouter_liste_ibfk_1` FOREIGN KEY (`id_utilisateur`) REFERENCES `compte_utilisateur` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ajouter_liste_ibfk_2` FOREIGN KEY (`id_utilisateur_ajoute`) REFERENCES `compte_utilisateur` (`id`) ON DELETE CASCADE
);

CREATE TABLE `transaction` (
  `id_transaction` int NOT NULL AUTO_INCREMENT,
  `montant` decimal(5,2) NOT NULL,
  `type` varchar(25) NOT NULL,
  `description` varchar(45) DEFAULT NULL,
  `date_transaction` datetime NOT NULL,
  `id_utilisateur` int NOT NULL,
  `id_utilisateur_paye` int DEFAULT NULL,
  PRIMARY KEY (`id_transaction`),
  KEY `id_utilisateur` (`id_utilisateur`),
  KEY `id_utilisateur_paye` (`id_utilisateur_paye`),
  CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`id_utilisateur`) REFERENCES `compte_utilisateur` (`id`) ON DELETE CASCADE,
  CONSTRAINT `transaction_ibfk_2` FOREIGN KEY (`id_utilisateur_paye`) REFERENCES `compte_utilisateur` (`id`) ON DELETE CASCADE
);