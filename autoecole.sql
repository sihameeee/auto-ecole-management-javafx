-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mar. 30 déc. 2025 à 07:43
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
  `date_inscription` date NOT NULL,
  PRIMARY KEY (`id`)
) ;

--
-- Déchargement des données de la table `condidats`
--

INSERT INTO `condidats` (`id`, `nom`, `prenom`, `adresse`, `telephone`, `date_naissance`, `etat`, `permis`, `disponibilite`, `date_inscription`) VALUES
(3, 'zemmour', 'sihame', 'bouira', '0500000000', '2004-09-07', 'Actif', 'A', 'disponible', '2025-12-17'),
(4, 'moad', 'kenza', 'sidi aich', '736526776', '2001-10-18', 'Actif', 'B', 'disponible', '2025-12-15'),
(5, 'bouamara', 'nesrine', 'bejaia', '0673892652', '2004-11-11', 'Actif', 'A', 'disponible', '2025-12-20'),
(6, 'saadi', 'ama', 'bejaia', '675849364', '2005-11-10', 'Actif', 'D', 'disponible', '2025-12-24'),
(9, 'azzouz', 'imene', 'akbou', '594876543', '2003-12-01', 'Non actif', 'B', 'disponible', '2025-12-12'),
(15, 'mesrane', 'malak', 'mayo', '567438262', '2001-12-05', 'Non actif', 'B', 'disponible', '2025-12-21'),
(11, 'aimene', 'lilia', 'oued ghir', '0567847363', '2008-11-13', 'Non actif', 'E', 'disponible', '2025-12-08'),
(12, 'amrane', 'lyes', 'bejaia', '765437281', '2000-08-03', 'Actif', 'D', 'disponible', '2025-12-27'),
(13, 'abed', 'amine', 'amizour', '765432733', '1992-06-30', 'Non actif', 'E', 'disponible', '0000-00-00'),
(16, 'messaoudi', 'nabila', 'annaba', '0654323456', '2015-12-23', 'Non actif', 'C', 'disponible', '0000-00-00'),
(19, 'saoudi', 'wissal', 'lacaps', '0737356789', '2002-12-04', 'Non actif', 'B', 'disponible', '0000-00-00'),
(20, 'benali', 'mohammed', 'bejai', '0765432123', '1993-12-29', 'Actif', 'B', 'disponible', '2025-12-17'),
(23, 'dkdkkd', 'dddld', 'dldlldd', '0555555555', '2005-12-18', 'Non actif', 'B', 'disponible', '2025-12-20'),
(24, 'hdhdj', 'ldldldl', 'ldldld', '0555555555', '2000-12-03', 'Non actif', 'B', 'disponible', '2025-12-20'),
(25, 'jkfrhfhj', 'jhfdksefjkhe', 'jdjkdlkjfdkjfhjkjnnc', '0555555555', '2004-09-09', 'Non actif', 'B', 'disponible', '0000-00-00'),
(26, 'djdkdk', 'dkkdkd', 'kdkdmdm', '0566666666', '2000-03-04', 'Non actif', 'B', 'disponible', '0000-00-00');

--
-- Déclencheurs `condidats`
--
DROP TRIGGER IF EXISTS `set_date_inscription`;
DELIMITER $$
CREATE TRIGGER `set_date_inscription` BEFORE INSERT ON `condidats` FOR EACH ROW BEGIN
    IF NEW.date_inscription IS NULL THEN
        SET NEW.date_inscription = CURRENT_DATE;
    END IF;
END
$$
DELIMITER ;
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
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `configuration`
--

INSERT INTO `configuration` (`id`, `type_permis`, `prix`) VALUES
(1, 'B', 30000),
(6, 'A', 20000),
(5, 'C', 40000);

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
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `examen`
--

INSERT INTO `examen` (`id`, `date_examen`, `etat_examen`) VALUES
(1, '2025-12-16', 'Termine'),
(2, '2025-12-25', 'A venir'),
(3, '2025-12-24', 'Termine'),
(4, '2025-12-10', 'Termine'),
(5, '2025-12-09', 'Termine'),
(6, '2025-12-25', 'A venir'),
(7, '2025-12-24', 'Termine'),
(10, '2025-12-26', 'A venir'),
(9, '2025-12-19', 'Termine'),
(11, '2025-12-31', 'A venir'),
(12, '2025-12-16', 'Termine');

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
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `examen_candidat`
--

INSERT INTO `examen_candidat` (`id`, `examen_id`, `candidat_id`, `type_examen`, `decision`) VALUES
(1, 1, 1, 'creneau', 'gagnant'),
(3, 2, 4, 'creneau', NULL),
(4, 2, 5, 'code', NULL),
(8, 1, 3, 'creneau', 'Reussi'),
(9, 10, 6, 'creneau', 'En attente'),
(10, 2, 5, 'creneau', 'En attente'),
(11, 2, 5, 'code', 'Echoue'),
(12, 2, 5, 'creneau', 'En attente'),
(17, 1, 20, 'creneau', 'Reussi'),
(14, 1, 12, 'circulation', 'Reussi'),
(15, 1, 6, 'circulation', 'Reussi');

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
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `moniteur`
--

INSERT INTO `moniteur` (`id`, `nom`, `prenom`, `username`, `password`, `role`, `disponibilite`) VALUES
(1, 'zemmour', 'sihame', 'admin', 'admin', 'admin', 'disponible'),
(2, 'saadi', 'ama', 'ama', 'admin', 'user', 'disponible'),
(3, 'mechenane', 'nadir', 'utilisateur1', 'admin', 'user', 'disponible'),
(4, 'christiano', 'ronaldo', 'utilisateur1', 'realmadrid111', 'user', 'disponible'),
(9, 'zjdkd', 'fkfkkf', 'lkfjff', 'ffkfk11111', 'user', 'disponible'),
(6, 'tiha', 'real', 'tiha', '1234', 'user', 'disponible'),
(7, 'zemmour', 'djazira', 'djazira', 'admin1234', 'admin', 'disponible'),
(8, 'zemmour', 'djazira', 'djazira', 'admin1234', 'user', 'disponible');

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
) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `seance`
--

INSERT INTO `seance` (`id`, `date_seance`, `heure`, `condidat_id`, `moniteur_id`, `vehicule_id`, `typeseance`, `type`, `etat`) VALUES
(1, '2025-12-09', '14:00:00', 3, 2, 1, 'code', '', 'terminée'),
(2, '2025-12-31', '14:00:00', 4, 2, 6, 'creneau', 'B', 'A venir'),
(3, '2025-12-31', '11:00:00', 5, 3, 1, 'circulation', 'A', 'A venir'),
(4, '2026-01-01', '12:55:00', 3, 1, 6, 'creneau', 'A', 'A venir'),
(7, '2025-12-31', '04:00:00', 4, 4, 3, 'creneau', 'B', 'A venir'),
(8, '2025-12-10', '12:00:00', 3, 3, NULL, 'code', 'A', 'terminée'),
(24, '2025-12-26', '12:00:00', 24, 2, NULL, 'code', 'B', 'terminée'),
(19, '2025-12-26', '12:00:00', 16, 1, 1, 'creneau', 'C', 'terminée'),
(11, '2025-12-18', '12:00:00', 3, 1, NULL, 'code', '', 'terminée'),
(26, '2025-12-31', '12:00:00', 20, 4, 6, 'creneau', 'B', 'A venir'),
(13, '2025-12-31', '14:00:00', 15, 3, 1, 'creneau', 'B', 'A venir'),
(14, '2025-12-31', '12:00:00', 3, 2, 1, 'creneau', 'A', 'A venir'),
(15, '2025-12-31', '23:00:00', 12, 2, 3, 'creneau', 'D', 'A venir'),
(16, '2025-12-31', '00:00:00', 14, 4, 1, 'creneau', 'B', 'A venir'),
(18, '2025-12-31', '00:00:00', 20, 2, 6, 'circulation', 'B', 'A venir'),
(20, '2025-12-18', '12:00:00', 15, 3, 6, 'circulation', 'B', 'terminée'),
(22, '2025-12-19', '13:00:00', 14, 3, 1, 'circulation', 'B', 'terminée'),
(25, '2026-01-01', '13:00:00', 19, 6, 3, 'creneau', 'B', 'A venir'),
(27, '2025-12-24', '23:00:00', 6, 1, NULL, 'code', 'D', 'terminée'),
(28, '2025-12-24', '23:00:00', 4, 2, 1, 'creneau', 'B', 'terminée');

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

DROP EVENT IF EXISTS `maj_etat_examens`$$
CREATE DEFINER=`root`@`localhost` EVENT `maj_etat_examens` ON SCHEDULE EVERY 1 DAY STARTS '2025-12-24 03:48:43' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE examen
  SET etat_examen = 'Termine'
  WHERE date_examen < CURDATE()
    AND etat_examen != 'Termine'$$

DROP EVENT IF EXISTS `maj_etat_condidats`$$
CREATE DEFINER=`root`@`localhost` EVENT `maj_etat_condidats` ON SCHEDULE EVERY 1 DAY STARTS '2025-12-24 03:51:01' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE condidats c
  SET c.etat = 'non actif'
  WHERE c.etat != 'non actif'
    AND NOT EXISTS (
        SELECT 1
        FROM examen_candidat ec
        JOIN examen e ON ec.examen_id = e.id
        WHERE ec.candidat_id = c.id
          AND e.date_examen >= DATE_SUB(CURDATE(), INTERVAL 1 YEAR)
    )$$

DROP EVENT IF EXISTS `maj_disponibilite`$$
CREATE DEFINER=`root`@`localhost` EVENT `maj_disponibilite` ON SCHEDULE EVERY 1 MINUTE STARTS '2025-12-24 04:01:01' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
    -- Candidat
    UPDATE condidats c
    SET c.disponibilite = 'non disponible'
    WHERE EXISTS (
        SELECT 1
        FROM seance s
        WHERE s.condidat_id = c.id
          AND NOW() BETWEEN TIMESTAMP(s.date_seance, s.heure) 
                        AND TIMESTAMP(s.date_seance, s.heure) + INTERVAL 1 HOUR
    );

    UPDATE condidats c
    SET c.disponibilite = 'disponible'
    WHERE NOT EXISTS (
        SELECT 1
        FROM seance s
        WHERE s.condidat_id = c.id
          AND NOW() BETWEEN TIMESTAMP(s.date_seance, s.heure) 
                        AND TIMESTAMP(s.date_seance, s.heure) + INTERVAL 1 HOUR
    );

    -- Véhicule
    UPDATE vehicules v
    SET v.disponibilite = 'non disponible'
    WHERE EXISTS (
        SELECT 1
        FROM seance s
        WHERE s.vehicule_id = v.id
          AND NOW() BETWEEN TIMESTAMP(s.date_seance, s.heure) 
                        AND TIMESTAMP(s.date_seance, s.heure) + INTERVAL 1 HOUR
    );

    UPDATE vehicules v
    SET v.disponibilite = 'disponible'
    WHERE NOT EXISTS (
        SELECT 1
        FROM seance s
        WHERE s.vehicule_id = v.id
          AND NOW() BETWEEN TIMESTAMP(s.date_seance, s.heure) 
                        AND TIMESTAMP(s.date_seance, s.heure) + INTERVAL 1 HOUR
    );

    -- Moniteur
    UPDATE moniteur m
    SET m.disponibilite = 'non disponible'
    WHERE EXISTS (
        SELECT 1
        FROM seance s
        WHERE s.moniteur_id = m.id
          AND NOW() BETWEEN TIMESTAMP(s.date_seance, s.heure) 
                        AND TIMESTAMP(s.date_seance, s.heure) + INTERVAL 1 HOUR
    );

    UPDATE moniteur m
    SET m.disponibilite = 'disponible'
    WHERE NOT EXISTS (
        SELECT 1
        FROM seance s
        WHERE s.moniteur_id = m.id
          AND NOW() BETWEEN TIMESTAMP(s.date_seance, s.heure) 
                        AND TIMESTAMP(s.date_seance, s.heure) + INTERVAL 1 HOUR
    );
END$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
