-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: May 13, 2017 at 11:24 PM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `searchengineph2`
--

-- --------------------------------------------------------

--
-- Table structure for table `expressionscounts`
--

CREATE TABLE IF NOT EXISTS `expressionscounts` (
  `Expression` varchar(10000) NOT NULL,
  `Doc_id` bigint(20) NOT NULL,
  `tf_importance` double NOT NULL,
  `updated` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `record`
--

CREATE TABLE IF NOT EXISTS `record` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `URL` text NOT NULL,
  `Visted` tinyint(1) NOT NULL,
  `file` tinyint(1) NOT NULL,
  `invalid` tinyint(1) NOT NULL,
  `Title` varchar(1000) DEFAULT NULL,
  `rank` double NOT NULL,
  `updated` tinyint(1) NOT NULL,
  `indexed` tinyint(1) NOT NULL,
  `Diff` double NOT NULL,
  `To_Rank` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Table structure for table `restrictedurls`
--

CREATE TABLE IF NOT EXISTS `restrictedurls` (
  `ulr` text NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1013 ;

-- --------------------------------------------------------

--
-- Table structure for table `word`
--

CREATE TABLE IF NOT EXISTS `word` (
  `Word` varchar(500) NOT NULL,
  `Doc_id` bigint(20) NOT NULL,
  `Stemming` varchar(500) NOT NULL,
  `importance` int(11) NOT NULL,
  `updated` int(11) NOT NULL,
  `TF` double NOT NULL,
  PRIMARY KEY (`Word`,`Doc_id`),
  KEY `word` (`Word`),
  KEY `Doc_id` (`Doc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
