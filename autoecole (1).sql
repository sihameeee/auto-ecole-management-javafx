-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : lun. 01 déc. 2025 à 23:01
-- Version du serveur : 9.1.0
-- Version de PHP : 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `autoecole`
--

-- --------------------------------------------------------

--
-- Structure de la table `condidats`
--

DROP TABLE IF EXISTS `condidats`;
CREATE TABLE IF NOT EXISTS `condidats` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(30) NOT NULL,
  `prenom` varchar(30) NOT NULL,
  `adresse` varchar(100) NOT NULL,
  `telephone` int NOT NULL,
  `date_naissance` date NOT NULL,
  `etat` enum('Actif','Non actif') NOT NULL,
  `permis` enum('A','B','C','D','E') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `disponibilite` enum('disponible','non disponible') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `condidats`
--

INSERT INTO `condidats` (`id`, `nom`, `prenom`, `adresse`, `telephone`, `date_naissance`, `etat`, `permis`, `disponibilite`) VALUES
(3, 'zemmour', 'sihame', 'bouira', 563728792, '2004-09-07', 'Actif', 'B', 'disponible'),
(4, 'moad', 'kenza', 'sidi aich', 736526776, '2001-10-18', 'Actif', 'B', 'disponible'),
(5, 'nouamara', 'nesrine', 'bejaia', 673892652, '2004-11-11', 'Actif', 'A', 'disponible'),
(6, 'saadi', 'ama', 'bejaia', 675849364, '2005-11-10', 'Actif', 'D', 'disponible'),
(7, 'smith', 'sara', 'usa', 657489393, '1996-11-13', 'Actif', 'B', 'disponible'),
(8, 'adams', 'wednesday', 'uk', 653748635, '2001-11-15', 'Actif', 'B', 'disponible'),
(9, 'azzouz', 'imene', 'akbou', 594876543, '2003-12-01', 'Actif', 'B', 'disponible'),
(10, 'malak', 'malak', 'bejaia', 776587465, '2003-11-24', 'Actif', 'B', 'disponible'),
(11, 'aimene', 'lilia', 'oued ghir', 567847363, '2000-11-16', 'Actif', 'E', 'disponible');

-- --------------------------------------------------------

--
-- Structure de la table `configuration`
--

DROP TABLE IF EXISTS `configuration`;
CREATE TABLE IF NOT EXISTS `configuration` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `type_permis` enum('A','B','C','D','E') NOT NULL,
  `prix` decimal(10,0) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `configuration`
--

INSERT INTO `configuration` (`id`, `type_permis`, `prix`) VALUES
(1, 'B', 30000);

-- --------------------------------------------------------

--
-- Structure de la table `examen`
--

DROP TABLE IF EXISTS `examen`;
CREATE TABLE IF NOT EXISTS `examen` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_examen` date NOT NULL,
  `etat_examen` enum('annule','en_cours','termine','a_venir') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `examen`
--

INSERT INTO `examen` (`id`, `date_examen`, `etat_examen`) VALUES
(1, '2025-12-16', 'a_venir'),
(2, '2025-12-25', 'a_venir');

-- --------------------------------------------------------

--
-- Structure de la table `examen_candidat`
--

DROP TABLE IF EXISTS `examen_candidat`;
CREATE TABLE IF NOT EXISTS `examen_candidat` (
  `id` int NOT NULL AUTO_INCREMENT,
  `examen_id` int NOT NULL,
  `candidat_id` int NOT NULL,
  `type_examen` enum('code','creneau','circulation') NOT NULL,
  `decision` enum('gagnant','perdant') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `examen_id` (`examen_id`),
  KEY `candidat_id` (`candidat_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `examen_candidat`
--

INSERT INTO `examen_candidat` (`id`, `examen_id`, `candidat_id`, `type_examen`, `decision`) VALUES
(1, 1, 1, 'creneau', 'gagnant'),
(2, 1, 3, 'creneau', NULL),
(3, 2, 4, 'creneau', NULL),
(4, 2, 5, 'code', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `moniteur`
--

DROP TABLE IF EXISTS `moniteur`;
CREATE TABLE IF NOT EXISTS `moniteur` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `nom` varchar(30) NOT NULL,
  `prenom` varchar(30) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `role` enum('admin','user') NOT NULL,
  `disponibilite` enum('disponible','non disponible') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `moniteur`
--

INSERT INTO `moniteur` (`id`, `nom`, `prenom`, `username`, `password`, `role`, `disponibilite`) VALUES
(1, 'zemmour', 'sihame', 'admin', 'admin', 'admin', 'disponible'),
(2, 'saadi', 'ama', 'ama', 'admin', 'user', 'disponible'),
(3, 'mechenane', 'nadir', 'utilisateur1', 'admin', 'user', 'disponible'),
(4, 'christiano', 'ronaldo', 'utilisateur1', 'realmadrid', 'user', 'disponible');

-- --------------------------------------------------------

--
-- Structure de la table `paiement`
--

DROP TABLE IF EXISTS `paiement`;
CREATE TABLE IF NOT EXISTS `paiement` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `condidat_id` int NOT NULL,
  `date_paiement` date NOT NULL,
  `montant_total` decimal(10,2) NOT NULL,
  `montant_paye` decimal(10,2) NOT NULL,
  `montant_restant` decimal(10,2) DEFAULT NULL,
  `etat` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `condidat_id` (`condidat_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `paiement`
--

INSERT INTO `paiement` (`id`, `condidat_id`, `date_paiement`, `montant_total`, `montant_paye`, `montant_restant`, `etat`) VALUES
(1, 7, '2025-12-01', 30000.00, 15000.00, 15000.00, 'partiel');

-- --------------------------------------------------------

--
-- Structure de la table `seance`
--

DROP TABLE IF EXISTS `seance`;
CREATE TABLE IF NOT EXISTS `seance` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `date_seance` date NOT NULL,
  `heure` time NOT NULL,
  `condidat_id` int NOT NULL,
  `moniteur_id` int NOT NULL,
  `vehicule_id` int NOT NULL,
  `type` varchar(50) NOT NULL,
  `etat` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `condidat_id` (`condidat_id`),
  KEY `moniteur_id` (`moniteur_id`),
  KEY `vehicule_id` (`vehicule_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `seance`
--

INSERT INTO `seance` (`id`, `date_seance`, `heure`, `condidat_id`, `moniteur_id`, `vehicule_id`, `type`, `etat`) VALUES
(1, '2025-12-09', '14:00:00', 3, 2, 1, 'code', 'a venir'),
(2, '2025-12-01', '14:00:00', 4, 2, 1, 'code', 'terminee'),
(3, '2025-12-02', '11:00:00', 5, 3, 1, 'code', 'a venir');

-- --------------------------------------------------------

--
-- Structure de la table `vehicules`
--

DROP TABLE IF EXISTS `vehicules`;
CREATE TABLE IF NOT EXISTS `vehicules` (
  `id` int NOT NULL AUTO_INCREMENT,
  `marque` varchar(30) NOT NULL,
  `matricule` varchar(30) NOT NULL,
  `type` enum('voiture','moto','camion','bus') NOT NULL,
  `disponibilite` enum('disponible','non disponible','','') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `vehicules`
--

INSERT INTO `vehicules` (`id`, `marque`, `matricule`, `type`, `disponibilite`) VALUES
(1, 'kia', '9847632006', 'voiture', 'disponible');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
