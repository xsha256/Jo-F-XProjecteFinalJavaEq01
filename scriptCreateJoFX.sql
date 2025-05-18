CREATE DATABASE IF NOT EXISTS `jofx`;
USE `jofx`;
CREATE TABLE IF NOT EXISTS `usuari` (
    `id` INT AUTO_INCREMENT,
    `nom` VARCHAR(50) NOT NULL,
    `cognoms` VARCHAR(50) NOT NULL,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `imatge` BLOB,
    `constrasenya` VARCHAR(100) NOT NULL,
    `poblacio` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `pixelArt` (
    `id` INT AUTO_INCREMENT,
    `idUsuari` INT NOT NULL,
    `data` DATETIME NOT NULL,
    `dibuix` LONGTEXT NOT NULL,
    PRIMARY KEY (`id`),
    fOREIGN KEY (`idUsuari`) REFERENCES `usuari`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS `pescaMines` (
    `id` INT AUTO_INCREMENT,
    `idUsuari` INT NOT NULL,
    `data` DATETIME NOT NULL,
    `sesionJuego` LONGTEXT NOT NULL,
    `acabat` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`id`),
    fOREIGN KEY (`idUsuari`) REFERENCES `usuari`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS `wordle` (
    `id` INT AUTO_INCREMENT,
    `idUsuari` INT NOT NULL,
    `intents` INT NOT NULL,
    `fallades` INT NOT NULL,
    `encertats` INT NOT NULL,
    PRIMARY KEY (`id`),
    fOREIGN KEY (`idUsuari`) REFERENCES `usuari`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS `paraulesFetes` (
    `id` INT AUTO_INCREMENT,
    `idWordle` INT NOT NULL,
    `paraula` INT NOT NULL,
    PRIMARY KEY (`id`),
    fOREIGN KEY (`idWordle`) REFERENCES `wordle`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);