CREATE DATABASE IF NOT EXISTS redsocial;
USE redsocial;
CREATE TABLE IF NOT EXISTS `user` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(20) NOT NULL,
    `password` varchar(256) NOT NULL,
    `avatar` longblob NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
    );

CREATE TABLE IF NOT EXISTS `post`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` int(11) NOT NULL,
    `texto` varchar(100) NOT NULL,
    `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `fecha_modificacion` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `id_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS `comments`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `id_user` int(11) NOT NULL,
    `id_post` int(11) NOT NULL,
    `texto` varchar(100) NOT NULL,
    `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `id_user_comment` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `id_post_comment` FOREIGN KEY (`id_post`) REFERENCES `post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS `likes`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `id_user` int(11) NOT NULL,
    `id_post` int(11) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `likeID` (`id_user`,`id_post`),
    CONSTRAINT `user_like` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`),
    CONSTRAINT `post_like` FOREIGN KEY (`id_post`) REFERENCES `post` (`id`)
    );

CREATE TABLE IF NOT EXISTS `follower`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `id_follow` int(11) NOT NULL,
    `id_follower` int(11) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `follow` (`id_follow`,`id_follower`),
    CONSTRAINT `id_user_follower` FOREIGN KEY (`id_follower`) REFERENCES `user` (`id`),
    CONSTRAINT `id_follow` FOREIGN KEY (`id_follow`) REFERENCES `user` (`id`)
    );