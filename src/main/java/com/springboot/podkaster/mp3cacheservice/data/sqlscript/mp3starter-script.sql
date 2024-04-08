CREATE DATABASE  IF NOT EXISTS `podkaster_service`;
USE `podkaster_service`;


DROP TABLE IF EXISTS `mp3_detail`;

CREATE TABLE `mp3_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `source_id` int DEFAULT NULL,
  `uri` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;