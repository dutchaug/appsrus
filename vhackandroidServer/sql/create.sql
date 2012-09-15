CREATE USER 'vhackandroid'@'localhost' IDENTIFIED BY  'appsRus';

GRANT USAGE ON * . * TO  'vhackandroid'@'localhost' IDENTIFIED BY  'appsRus' WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0 ;

CREATE DATABASE IF NOT EXISTS  `vhackandroid` ;

-- phpMyAdmin SQL Dump
-- version 3.4.11deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 15, 2012 at 06:49 PM
-- Server version: 5.5.24
-- PHP Version: 5.4.4-2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `vhackandroid`
--

-- --------------------------------------------------------

--
-- Table structure for table `congrats`
--

CREATE TABLE IF NOT EXISTS `congrats` (
  `fromUserId` int(11) NOT NULL,
  `toUserId` int(11) NOT NULL,
  `year` year(4) NOT NULL,
  UNIQUE KEY `oncePerYear` (`fromUserId`,`toUserId`,`year`),
  KEY `fromUserId` (`fromUserId`),
  KEY `toUserId` (`toUserId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `congrats`
--

INSERT INTO `congrats` (`fromUserId`, `toUserId`, `year`) VALUES
(4, 11, 2012);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(128) NOT NULL,
  `firstName` varchar(64) NOT NULL DEFAULT '',
  `lastName` varchar(64) NOT NULL,
  `tagline` varchar(256) NOT NULL DEFAULT '',
  `phoneModel` varchar(128) NOT NULL,
  `osVersion` varchar(128) NOT NULL,
  `authToken` varchar(64) NOT NULL,
  `birthday` date NOT NULL,
  `month` int(11) NOT NULL,
  `day` int(11) NOT NULL,
  `timeDiff` int(11) NOT NULL,
  `serverTime` datetime NOT NULL,
  `c2dmToken` varchar(128) NOT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `email` (`email`),
  KEY `month` (`month`,`day`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=31 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userId`, `email`, `firstName`, `lastName`, `tagline`, `phoneModel`, `osVersion`, `authToken`, `birthday`, `month`, `day`, `timeDiff`, `serverTime`, `c2dmToken`) VALUES
(4, 'sla.shalafi@gmail.com', '', '', '', '', '', 'cOpN+CB7eltnmQNrayvdEyl0lxAVg0u4', '1976-11-18', 11, 18, 0, '0000-00-00 00:00:00', '654321'),
(10, 'dummyuiser1@appsrus.nl', 'Dummy', 'User1', 'I''m the first test user, and I love receiving Android Birthday wishes', 'Samsung Galaxy S4', 'Jelly Bean 4.1.1.1', '', '2012-09-15', 9, 15, 0, '0000-00-00 00:00:00', ''),
(11, 'dummyuser2@appsrus.nl', 'Dummy', 'User2', 'I''m the other test user', '', '', '', '1996-09-15', 9, 15, 0, '0000-00-00 00:00:00', ''),
(13, 'test@example.com', '', '', '', '', '', 'YCUz1XpoFO9LOZ5xI+kGcC2b6qtFdALc', '0000-00-00', 0, 0, 0, '0000-00-00 00:00:00', ''),
(14, 'vdlingen@gmail.com', 'Ronald', '', '', 'All Android phones, well, just 30', '4.1 And beyond!', 'X7QDajDTlnppEkvjbeWRvk1zczAUY7AZ', '0000-00-00', 0, 0, 0, '0000-00-00 00:00:00', ''),
(15, 'ronald@layar.com', '', '', '', '', '', 'FMrW+RSpYxFtjHhcBDjwpzlaQjhhLR9o', '0000-00-00', 0, 0, 0, '0000-00-00 00:00:00', '');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
