-- phpMyAdmin SQL Dump
-- version 4.2.10
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Mar 22, 2017 at 02:02 PM
-- Server version: 5.5.38
-- PHP Version: 5.6.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `kidsdiary`
--

-- --------------------------------------------------------

--
-- Table structure for table `activities`
--

CREATE TABLE `activities` (
`id` bigint(11) NOT NULL,
  `parentId` int(11) NOT NULL,
  `name` varchar(35) COLLATE utf8_unicode_ci NOT NULL,
  `icon` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `score` tinyint(2) NOT NULL DEFAULT '0',
  `note` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `isActivated` tinyint(2) NOT NULL DEFAULT '0',
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `deletedAt` datetime DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `activities`
--

INSERT INTO `activities` (`id`, `parentId`, `name`, `icon`, `score`, `note`, `isActivated`, `createdAt`, `deletedAt`) VALUES
(1, 17, 'Eat1', 'upload/images/activities/eat.png', 3, 'Eating note1', 0, '2017-03-22 06:01:44', '2017-03-22 13:28:55');

-- --------------------------------------------------------

--
-- Table structure for table `countries`
--

CREATE TABLE `countries` (
`id` int(11) NOT NULL,
  `name` varchar(35) COLLATE utf8_unicode_ci NOT NULL,
  `dialingCode` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` datetime DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `countries`
--

INSERT INTO `countries` (`id`, `name`, `dialingCode`, `createdAt`, `deletedAt`) VALUES
(1, 'Cam', ' 88', '2017-02-23 16:15:26', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `daily_activities`
--

CREATE TABLE `daily_activities` (
`id` bigint(20) NOT NULL,
  `parent` bigint(20) NOT NULL,
  `child` bigint(20) NOT NULL,
  `activityDate` datetime NOT NULL,
  `isApproved` tinyint(2) NOT NULL DEFAULT '0',
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deletedAt` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `daily_activity_details`
--

CREATE TABLE `daily_activity_details` (
`id` bigint(20) NOT NULL,
  `activity` bigint(20) NOT NULL,
  `dailyActivity` bigint(20) NOT NULL,
  `score` tinyint(2) NOT NULL DEFAULT '0',
  `isApproved` tinyint(2) NOT NULL DEFAULT '0',
  `note` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deletedAt` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `families`
--

CREATE TABLE `families` (
`id` bigint(20) NOT NULL,
  `father` bigint(20) NOT NULL,
  `mother` bigint(20) NOT NULL,
  `child` bigint(20) NOT NULL,
  `note` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deletedAt` datetime DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `families`
--

INSERT INTO `families` (`id`, `father`, `mother`, `child`, `note`, `createdAt`, `deletedAt`) VALUES
(4, 17, 0, 23, 'Child 1', '2017-03-20 16:23:18', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
`id` int(11) NOT NULL,
  `username` varchar(35) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(35) COLLATE utf8_unicode_ci NOT NULL,
  `accessToken` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `loggedinDate` datetime DEFAULT NULL,
  `email` varchar(35) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(35) COLLATE utf8_unicode_ci NOT NULL,
  `firstName` varchar(35) COLLATE utf8_unicode_ci NOT NULL,
  `lastName` varchar(35) COLLATE utf8_unicode_ci NOT NULL,
  `sex` varchar(2) COLLATE utf8_unicode_ci NOT NULL,
  `birthDate` date DEFAULT NULL,
  `country` int(11) DEFAULT NULL,
  `userType` int(11) NOT NULL,
  `isActivated` tinyint(2) NOT NULL DEFAULT '0',
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deletedAt` datetime DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `accessToken`, `loggedinDate`, `email`, `phone`, `firstName`, `lastName`, `sex`, `birthDate`, `country`, `userType`, `isActivated`, `createdAt`, `deletedAt`) VALUES
(17, 'nguonchhay', '49-50-51-52-53', '7254', NULL, 'nguonchhay.touch@gmail.com', '', 'firstname', 'lastname', 'M', '1991-05-06', 1, 1, 0, '2017-03-20 11:26:52', NULL),
(23, 'san', '49-50-51-52-53', NULL, NULL, '', '012', 'Sok', 'San', 'M', '2012-12-31', 1, 3, 0, '2017-03-20 16:23:18', NULL),
(27, 'long', '49-50-51-52-53', '8611', NULL, 'nguonchhay.touch@gmail.com', '', 'firstname', 'lastname', 'M', '1970-01-01', 1, 1, 0, '2017-03-21 12:20:41', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `user_types`
--

CREATE TABLE `user_types` (
  `id` int(11) NOT NULL,
  `type` varchar(35) COLLATE utf8_unicode_ci NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deletedAt` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `user_types`
--

INSERT INTO `user_types` (`id`, `type`, `createdAt`, `deletedAt`) VALUES
(1, 'Wife', '2017-01-31 17:00:00', NULL),
(2, 'Husband', '2017-01-31 17:00:00', NULL),
(3, 'Child', '2017-01-31 17:00:00', NULL),
(4, 'Adopter', '2017-01-31 17:00:00', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `activities`
--
ALTER TABLE `activities`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `countries`
--
ALTER TABLE `countries`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `daily_activities`
--
ALTER TABLE `daily_activities`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `daily_activity_details`
--
ALTER TABLE `daily_activity_details`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `families`
--
ALTER TABLE `families`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_types`
--
ALTER TABLE `user_types`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `activities`
--
ALTER TABLE `activities`
MODIFY `id` bigint(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `countries`
--
ALTER TABLE `countries`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `daily_activities`
--
ALTER TABLE `daily_activities`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `daily_activity_details`
--
ALTER TABLE `daily_activity_details`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `families`
--
ALTER TABLE `families`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=29;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
