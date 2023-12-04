--create database springsecurity;

use springsecurity;

CREATE TABLE `users`(
`id` INT NOT NULL AUTO_INCREMENT,
`username` VARCHAR(45) NOT NULL,
`password` VARCHAR(45) NOT NULL,
`enable` INT NOT NULL,
PRIMARY KEY(`id`));

CREATE TABLE `authorites`(
`id` INT NOT NULL AUTO_INCREMENT,
`username` VARCHAR(45) NOT NULL,
`authority` VARCHAR(45) NOT NULL,
PRIMARY KEY(`id`));

INSERT IGNORE INTO users VALUES(NULL, 'happy', '12345', '1');
INSERT IGNORE INTO authorites VALUES(NULL, 'happy', 'write');