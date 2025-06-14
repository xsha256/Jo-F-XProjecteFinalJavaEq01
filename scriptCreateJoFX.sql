CREATE DATABASE IF NOT EXISTS `jofx`;
USE `jofx`;
CREATE TABLE IF NOT EXISTS `usuari` (
    `id` INT AUTO_INCREMENT,
    `nom` VARCHAR(50) NOT NULL,
    `cognoms` VARCHAR(50) NOT NULL,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `imatge` LONGBLOB,
    `contrasenya` VARCHAR(100) NOT NULL,
    `poblacio` VARCHAR(50) NOT NULL,
    `salt` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `pixelArt` (
    `id` INT AUTO_INCREMENT,
    `idUsuari` INT NOT NULL,
    `data` DATETIME NOT NULL,
    `dibuix` LONGBLOB NOT NULL,
    PRIMARY KEY (`id`),
    fOREIGN KEY (`idUsuari`) REFERENCES `usuari`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS `pescaMines` (
    `id` INT AUTO_INCREMENT,
    `idUsuari` INT NOT NULL,
    `data` DATETIME NOT NULL,
    `sesioJoc` LONGBLOB NOT NULL,
    `tamany` VARCHAR(20) NOT NULL,
    `temps` DOUBLE NOT NULL,
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
    `paraula` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`),
    fOREIGN KEY (`idWordle`) REFERENCES `wordle`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
