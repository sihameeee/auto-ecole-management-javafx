-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mar. 23 déc. 2025 à 12:52
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
  `telephone` varchar(10) NOT NULL,
  `date_naissance` date NOT NULL,
  `etat` enum('Actif','Non actif') NOT NULL,
  `permis` enum('A','B','C','D','E') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `disponibilite` enum('disponible','non disponible') NOT NULL,
  PRIMARY KEY (`id`)
) ;

--
-- Déchargement des données de la table `condidats`
--

INSERT INTO `condidats` (`id`, `nom`, `prenom`, `adresse`, `telephone`, `date_naissance`, `etat`, `permis`, `disponibilite`) VALUES
(3, 'zemmour', 'sihame', 'bouira', '0500000000', '2004-09-07', 'Actif', 'A', 'disponible'),
(4, 'moad', 'kenza', 'sidi aich', '736526776', '2001-10-18', 'Actif', 'B', 'disponible'),
(5, 'bouamara', 'nesrine', 'bejaia', '0673892652', '2004-11-11', 'Actif', 'A', 'disponible'),
(6, 'saadi', 'ama', 'bejaia', '675849364', '2005-11-10', 'Actif', 'D', 'disponible'),
(21, 'zemmour', 'sihame', 'bouira', '0788888888', '2000-12-13', 'Actif', 'B', 'disponible'),
(9, 'azzouz', 'imene', 'akbou', '594876543', '2003-12-01', 'Actif', 'B', 'disponible'),
(15, 'mesrane', 'malak', 'mayo', '567438262', '2001-12-05', 'Actif', 'B', 'disponible'),
(11, 'aimene', 'lilia', 'oued ghir', '0567847363', '2008-11-13', 'Actif', 'E', 'disponible'),
(12, 'amrane', 'lyes', 'bejaia', '765437281', '2000-08-03', 'Actif', 'D', 'disponible'),
(13, 'abed', 'amine', 'amizour', '765432733', '1992-06-30', 'Actif', 'E', 'disponible'),
(16, 'messaoudi', 'nabila', 'annaba', '0654323456', '2015-12-23', 'Actif', 'C', 'disponible'),
(19, 'saoudi', 'wissal', 'lacaps', '0737356789', '2002-12-04', 'Actif', 'B', 'disponible'),
(20, 'benali', 'mohammed', 'bejai', '0765432123', '1993-12-29', 'Actif', 'B', 'disponible'),
(23, 'dkdkkd', 'dddld', 'dldlldd', '0555555555', '2005-12-18', 'Actif', 'B', 'disponible'),
(24, 'hdhdj', 'ldldldl', 'ldldld', '0555555555', '2000-12-03', 'Actif', 'B', 'disponible'),
(25, 'jkfrhfhj', 'jhfdksefjkhe', 'jdjkdlkjfdkjfhjkjnnc', '0555555555', '2004-09-09', 'Actif', 'B', 'disponible'),
(26, 'djdkdk', 'dkkdkd', 'kdkdmdm', '0566666666', '2000-03-04', 'Actif', 'B', 'disponible');

--
-- Déclencheurs `condidats`
--
DROP TRIGGER IF EXISTS `trg_age`;
DELIMITER $$
CREATE TRIGGER `trg_age` BEFORE INSERT ON `condidats` FOR EACH ROW BEGIN
    IF TIMESTAMPDIFF(YEAR, NEW.date_naissance, CURDATE()) < 17 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Erreur : le candidat doit avoir au moins 17 ans.';
    END IF;
END
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `trg_nom`;
DELIMITER $$
CREATE TRIGGER `trg_nom` BEFORE INSERT ON `condidats` FOR EACH ROW BEGIN
    IF NEW.nom NOT REGEXP '^[A-Za-zÀ-ÿ]{2,}$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Nom invalide : lettres uniquement, minimum 2 caractères.';
    END IF;

    IF NEW.prenom NOT REGEXP '^[A-Za-zÀ-ÿ]{2,}$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Prénom invalide : lettres uniquement, minimum 2 caractères.';
    END IF;
END
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `trg_tel`;
DELIMITER $$
CREATE TRIGGER `trg_tel` BEFORE INSERT ON `condidats` FOR EACH ROW BEGIN
    IF NEW.telephone NOT REGEXP '^(05|06|07)[0-9]{8}$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Numéro de téléphone invalide : doit commencer par 05, 06 ou 07 et contenir 10 chiffres.';
    END IF;
END
$$
DELIMITER ;

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
  `etat_examen` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `examen`
--

INSERT INTO `examen` (`id`, `date_examen`, `etat_examen`) VALUES
(1, '2025-12-16', 'A venir'),
(2, '2025-12-25', 'A venir'),
(3, '2025-12-24', 'A venir'),
(4, '2025-12-10', 'Annuler'),
(5, '2025-12-09', 'A venir'),
(6, '2025-12-25', 'A venir'),
(7, '2025-12-24', 'Termine'),
(10, '2025-12-26', 'A venir'),
(9, '2025-12-19', 'A venir');

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
  `decision` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `examen_id` (`examen_id`),
  KEY `candidat_id` (`candidat_id`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `examen_candidat`
--

INSERT INTO `examen_candidat` (`id`, `examen_id`, `candidat_id`, `type_examen`, `decision`) VALUES
(1, 1, 1, 'creneau', 'gagnant'),
(2, 1, 3, 'creneau', 'Echoue'),
(3, 2, 4, 'creneau', NULL),
(4, 2, 5, 'code', NULL),
(5, 1, 4, 'creneau', 'En attente'),
(6, 1, 4, 'circulation', 'En attente'),
(7, 1, 4, 'creneau', 'Echoue'),
(8, 1, 3, 'creneau', 'En attente'),
(9, 10, 6, 'creneau', 'En attente'),
(10, 2, 5, 'creneau', 'En attente'),
(11, 2, 5, 'code', 'Echoue'),
(12, 2, 5, 'creneau', 'En attente'),
(13, 10, 6, 'creneau', 'Reussi'),
(14, 1, 12, 'circulation', 'En attente');

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
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `moniteur`
--

INSERT INTO `moniteur` (`id`, `nom`, `prenom`, `username`, `password`, `role`, `disponibilite`) VALUES
(1, 'zemmour', 'sihame', 'admin', 'admin', 'admin', 'disponible'),
(2, 'saadi', 'ama', 'ama', 'admin', 'user', 'disponible'),
(3, 'mechenane', 'nadir', 'utilisateur1', 'admin', 'user', 'disponible'),
(4, 'christiano', 'ronaldo', 'utilisateur1', 'realmadrid', 'user', 'disponible'),
(5, 'hello', 'word', 'admiiin', 'admiiin', 'user', 'disponible'),
(6, 'tiha', 'real', 'tiha', '1234', 'user', 'disponible');

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
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `paiement`
--

INSERT INTO `paiement` (`id`, `condidat_id`, `date_paiement`, `montant_total`, `montant_paye`, `montant_restant`, `etat`) VALUES
(1, 7, '2025-12-01', 30000.00, 15000.00, 15000.00, 'partiel'),
(2, 3, '2025-12-17', 30000.00, 15000.00, 15000.00, 'partiel'),
(3, 4, '2025-12-22', 30000.00, 20000.00, 10000.00, 'Partiel'),
(4, 5, '2025-12-23', 0.00, 0.00, 0.00, 'Partiel'),
(6, 20, '2025-12-23', 30000.00, 0.00, 30000.00, 'En attente');

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
  `vehicule_id` int DEFAULT NULL,
  `typeseance` varchar(20) NOT NULL,
  `type` varchar(50) NOT NULL,
  `etat` enum('En cours','A venir','terminée','Annulée') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `condidat_id` (`condidat_id`),
  KEY `moniteur_id` (`moniteur_id`),
  KEY `vehicule_id` (`vehicule_id`)
) ENGINE=MyISAM AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `seance`
--

INSERT INTO `seance` (`id`, `date_seance`, `heure`, `condidat_id`, `moniteur_id`, `vehicule_id`, `typeseance`, `type`, `etat`) VALUES
(1, '2025-12-09', '14:00:00', 3, 2, 1, 'code', '', 'terminée'),
(2, '2025-12-17', '14:00:00', 4, 2, NULL, 'code', 'B', 'terminée'),
(3, '2025-12-19', '11:00:00', 5, 3, 1, 'circulation', 'A', 'terminée'),
(4, '2026-01-01', '12:55:00', 3, 1, 6, 'creneau', 'A', 'A venir'),
(7, '2025-12-17', '00:00:00', 4, 4, 3, 'creneau', 'B', 'terminée'),
(8, '2025-12-10', '12:00:00', 3, 3, NULL, 'code', 'A', 'terminée'),
(9, '2025-12-26', '12:00:00', 13, 3, NULL, 'code', 'E', 'A venir'),
(19, '2025-12-26', '12:00:00', 16, 1, 1, 'creneau', 'C', 'A venir'),
(11, '2025-12-18', '12:00:00', 3, 1, NULL, 'code', '', 'terminée'),
(12, '2025-12-09', '06:00:00', 9, 4, 6, 'creneau', 'B', 'terminée'),
(13, '2025-12-19', '14:00:00', 15, 3, NULL, 'code', 'B', 'terminée'),
(14, '2025-12-31', '12:00:00', 3, 2, 1, 'creneau', 'A', 'A venir'),
(15, '2025-12-20', '23:00:00', 12, 2, NULL, 'code', 'D', 'terminée'),
(16, '2025-12-31', '00:00:00', 14, 4, 1, 'creneau', 'B', 'A venir'),
(18, '2025-12-31', '00:00:00', 20, 2, 6, 'circulation', 'B', 'A venir'),
(20, '2025-12-18', '12:00:00', 15, 3, 6, 'circulation', 'B', 'terminée'),
(22, '2025-12-19', '13:00:00', 14, 3, 1, 'circulation', 'B', 'terminée');

--
-- Déclencheurs `seance`
--
DROP TRIGGER IF EXISTS `trg_seance_before_insert`;
DELIMITER $$
CREATE TRIGGER `trg_seance_before_insert` BEFORE INSERT ON `seance` FOR EACH ROW BEGIN
    IF NEW.date_seance < NOW() THEN
        SET NEW.etat = 'Terminée';
    ELSE
        SET NEW.etat = 'A venir';
    END IF;
END
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `trg_seance_before_update`;
DELIMITER $$
CREATE TRIGGER `trg_seance_before_update` BEFORE UPDATE ON `seance` FOR EACH ROW BEGIN
    IF NEW.date_seance < NOW() THEN
        SET NEW.etat = 'Terminée';
    ELSE
        SET NEW.etat = 'A venir';
    END IF;
END
$$
DELIMITER ;

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
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `vehicules`
--

INSERT INTO `vehicules` (`id`, `marque`, `matricule`, `type`, `disponibilite`) VALUES
(1, 'kia', '9847632006', 'voiture', 'disponible'),
(2, 'lion', '12345-765', 'voiture', 'disponible'),
(3, '206', '9239924532', 'voiture', 'disponible'),
(4, 'lionnn', '1000000000', 'voiture', 'disponible'),
(6, 'toyota', '1234567890', 'bus', 'disponible');

DELIMITER $$
--
-- Évènements
--
DROP EVENT IF EXISTS `ev_update_etat_seance`$$
CREATE DEFINER=`root`@`localhost` EVENT `ev_update_etat_seance` ON SCHEDULE EVERY 1 MINUTE STARTS '2025-12-22 09:03:38' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE seance
    SET etat_seance = 'terminee'
    WHERE date_seance < NOW()
      AND etat_seance <> 'terminee'$$

DROP EVENT IF EXISTS `ev_update_etat_s`$$
CREATE DEFINER=`root`@`localhost` EVENT `ev_update_etat_s` ON SCHEDULE EVERY 1 MINUTE STARTS '2025-12-22 09:04:56' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE seance
    SET etat = 'terminee'
    WHERE date_seance < NOW()
      AND etat <> 'terminee'$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
