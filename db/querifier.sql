-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 05, 2012 at 01:54 PM
-- Server version: 5.5.24-log
-- PHP Version: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `querifier`
--

-- --------------------------------------------------------

--
-- Table structure for table `assignment`
--

CREATE TABLE IF NOT EXISTS `assignment` (
  `assgn_ID` int(10) NOT NULL,
  `num_ques` int(10) NOT NULL,
  `mode` set('learning','grading') NOT NULL,
  `deadline` timestamp NULL DEFAULT NULL,
  `post_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`assgn_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `assignment`
--

INSERT INTO `assignment` (`assgn_ID`, `num_ques`, `mode`, `deadline`, `post_time`, `name`) VALUES
(1, 1, 'learning', '2012-11-06 18:25:00', '2012-11-04 23:43:45', 'sql'),
(2, 2, 'learning', '2012-11-04 18:25:00', '2012-11-04 23:44:30', 'fgsjnjnd'),
(3, 1, 'grading', '2012-11-07 18:25:00', '2012-11-04 23:44:10', 'net'),
(4, 1, 'grading', '2012-11-05 18:25:00', '2012-11-04 23:43:13', 'storage');

-- --------------------------------------------------------

--
-- Table structure for table `news`
--

CREATE TABLE IF NOT EXISTS `news` (
  `news_ID` int(10) NOT NULL,
  `reply_ID` int(10) NOT NULL,
  `ID` int(10) NOT NULL,
  `sub` varchar(100) DEFAULT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `content` text NOT NULL,
  PRIMARY KEY (`news_ID`,`reply_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `news`
--

INSERT INTO `news` (`news_ID`, `reply_ID`, `ID`, `sub`, `time`, `content`) VALUES
(1, 0, 100050015, 'first news', '2012-11-04 20:23:01', 'hello!\r\nwhat''s up?\r\n\r\nAman Mangal'),
(1, 1, 100050010, NULL, '2012-11-04 21:35:43', 'hieeeeeeeeeeeeeeeeeeeee');

-- --------------------------------------------------------

--
-- Table structure for table `performance`
--

CREATE TABLE IF NOT EXISTS `performance` (
  `ID` int(10) NOT NULL,
  `assgn_ID` int(10) NOT NULL,
  `grade` int(11) NOT NULL,
  `time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`,`assgn_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `performance`
--

INSERT INTO `performance` (`ID`, `assgn_ID`, `grade`, `time`) VALUES
(100050010, 2, 9, '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `question`
--

CREATE TABLE IF NOT EXISTS `question` (
  `assgn_ID` int(10) NOT NULL,
  `ques_no` int(10) NOT NULL,
  `content` text NOT NULL,
  `answer` text NOT NULL,
  `database` varchar(20) NOT NULL,
  `ques_marks` int(10) NOT NULL DEFAULT '1',
  PRIMARY KEY (`assgn_ID`,`ques_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `question`
--

INSERT INTO `question` (`assgn_ID`, `ques_no`, `content`, `answer`, `database`, `ques_marks`) VALUES
(1, 1, 'this is question no 1!\r\nplease answer it!', 'this is the answwer!\r\nread it!', 'univ', 1),
(2, 1, 'this is question number 2', 'this is answer to the question no 2', 'univ', 1),
(2, 2, 'question 2 of 2', 'answer to question 2 of assignment 2', 'univ', 1),
(3, 1, 'this is 1 of 3', 'answer to 1 of 3', 'univ', 1),
(4, 1, '1 of 4', 'answer to 1 of 4', 'univ', 1);

-- --------------------------------------------------------

--
-- Table structure for table `submission`
--

CREATE TABLE IF NOT EXISTS `submission` (
  `ID` int(10) NOT NULL,
  `assgn_ID` int(10) NOT NULL,
  `Ques_no` int(10) NOT NULL,
  `ans_query` text NOT NULL,
  `submit_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `correct` tinyint(1) NOT NULL,
  `feedback` text NOT NULL,
  PRIMARY KEY (`ID`,`assgn_ID`,`Ques_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `dept_name` varchar(20) NOT NULL,
  `passwd` varchar(20) NOT NULL,
  `role` set('instructor','student') NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=100050016 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`ID`, `name`, `dept_name`, `passwd`, `role`) VALUES
(1, '1', 'CSE', '1', 'student'),
(1001, 'Amit', 'CSE', 'amit*', 'instructor'),
(100050010, 'Akshay Gaikwad', 'CSE', 'akshay', 'student'),
(100050015, 'Aman Mangal', 'CSE', 'amanmangal*', 'student');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
