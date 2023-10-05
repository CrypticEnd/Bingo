-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Oct 06, 2023 at 10:19 AM
-- Server version: 10.3.32-MariaDB
-- PHP Version: 8.0.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bingo`
--

-- --------------------------------------------------------

--
-- Table structure for table `bingo_mixes`
--

CREATE TABLE `bingo_mixes` (
  `mix_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `owner_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `bingo_mixes`
--

INSERT INTO `bingo_mixes` (`mix_id`, `name`, `owner_id`) VALUES
(1, 'Avengers', 1),
(2, 'Soccer Game', 1),
(3, 'Hamiltion (Musical)', 1),
(4, 'Avatar the last Airbender', 1),
(5, 'Numbers', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `bingo_mixes_seq`
--

CREATE TABLE `bingo_mixes_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `bingo_mixes_seq`
--

INSERT INTO `bingo_mixes_seq` (`next_val`) VALUES
(101);

-- --------------------------------------------------------

--
-- Table structure for table `games`
--

CREATE TABLE `games` (
  `game_id` int(11) NOT NULL,
  `join_code` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `owner_id` int(11) NOT NULL,
  `settings_id` int(11) NOT NULL,
  `winner_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `games`
--

INSERT INTO `games` (`game_id`, `join_code`, `name`, `owner_id`, `settings_id`, `winner_id`) VALUES
(1, 0, 'Not epic game', 1, 1, 5),
(2, 1111, 'Epic game', 1, 2, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `games_seq`
--

CREATE TABLE `games_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `games_seq`
--

INSERT INTO `games_seq` (`next_val`) VALUES
(101);

-- --------------------------------------------------------

--
-- Table structure for table `game_settings`
--

CREATE TABLE `game_settings` (
  `setting_id` int(11) NOT NULL,
  `all_same_words` bit(1) DEFAULT NULL,
  `board_color_primary` varchar(6) NOT NULL,
  `board_color_secondary` varchar(6) NOT NULL,
  `board_color_selected` varchar(6) NOT NULL,
  `board_size` int(11) NOT NULL,
  `center_free` bit(1) DEFAULT NULL,
  `mix_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `game_settings`
--

INSERT INTO `game_settings` (`setting_id`, `all_same_words`, `board_color_primary`, `board_color_secondary`, `board_color_selected`, `board_size`, `center_free`, `mix_id`) VALUES
(1, b'0', '3dfb99', '000000', 'ff224a', 5, b'0', 1),
(2, b'1', '3dfb99', '000000', 'ff224a', 5, b'1', 4);

-- --------------------------------------------------------

--
-- Table structure for table `game_settings_seq`
--

CREATE TABLE `game_settings_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `game_settings_seq`
--

INSERT INTO `game_settings_seq` (`next_val`) VALUES
(101);

-- --------------------------------------------------------

--
-- Table structure for table `phrases`
--

CREATE TABLE `phrases` (
  `pharse_id` int(11) NOT NULL,
  `color` varchar(6) NOT NULL,
  `font` varchar(255) NOT NULL,
  `phrase` varchar(255) NOT NULL,
  `mix_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `phrases`
--

INSERT INTO `phrases` (`pharse_id`, `color`, `font`, `phrase`, `mix_id`) VALUES
(1, '000000', 'Arial', 'Tony Drinks', 1),
(2, '000000', 'Arial', 'Thor throws his hammer', 1),
(3, '000000', 'Arial', 'Stark logo shown', 1),
(4, '000000', 'Arial', 'Loki tricks someone', 1),
(5, '000000', 'Arial', 'Samuel L. Jackson swears', 1),
(6, '000000', 'Arial', 'Bruce turns into the hulk', 1),
(7, '000000', 'Arial', 'Harkeye Misses', 1),
(8, '000000', 'Arial', 'A out of place quip', 1),
(9, '000000', 'Arial', 'Bad CGI', 1),
(10, '000000', 'Arial', 'Hulk smashes', 1),
(11, '000000', 'Arial', 'Rainbow Bridge is shown', 1),
(12, '000000', 'Arial', 'Reference another movie', 1),
(13, '000000', 'Arial', 'Trying to lift Thors hammer', 1),
(14, '000000', 'Arial', 'Someone is thrown across the room', 1),
(15, '000000', 'Arial', '\"We have a Hulk\"', 1),
(16, '000000', 'Arial', 'Infinity Stone', 1),
(17, '000000', 'Arial', 'The main theme plays', 1),
(18, '000000', 'Arial', 'Shield logo shown', 1),
(19, '000000', 'Arial', 'Suiting up', 1),
(20, '000000', 'Arial', 'Mind control is mentioned', 1),
(21, '000000', 'Arial', 'Someone says \"Tesseract\"', 1),
(22, '000000', 'Arial', '\"That\'s my secret, captain. I\'m always angry\"', 1),
(23, '000000', 'Arial', 'Chitauri mentioned', 1),
(24, '000000', 'Arial', 'Loki acts cocky', 1),
(25, '000000', 'Arial', 'Mook get beat up', 1),
(26, '000000', 'Arial', 'Yellow Card', 2),
(27, '000000', 'Arial', 'Red Card', 2),
(28, '000000', 'Arial', 'Fake Injury', 2),
(29, '000000', 'Arial', 'Goal Scored', 2),
(30, '000000', 'Arial', 'Challenging the referee', 2),
(31, '000000', 'Arial', 'Referee makes a \"stupid\" call', 2),
(32, '000000', 'Arial', 'Your team wins', 2),
(33, '000000', 'Arial', 'Your team loses', 2),
(34, '000000', 'Arial', 'One team is five points up', 2),
(35, '000000', 'Arial', 'Out of bounds ball', 2),
(36, '000000', 'Arial', 'Foul ball', 2),
(37, '000000', 'Arial', 'Goalkeeper makes a save', 2),
(38, '000000', 'Arial', 'Hand Ball', 2),
(39, '000000', 'Arial', 'Free Kick', 2),
(40, '000000', 'Arial', 'Penalty', 2),
(41, '000000', 'Arial', 'Someone is side-lined', 2),
(42, '000000', 'Arial', 'Announcer makes a mistake', 2),
(43, '000000', 'Arial', 'Announcer gets excited', 2),
(44, '000000', 'Arial', 'someone from the crowd runs onto the field', 2),
(45, '000000', 'Arial', 'Player yells at the referee', 2),
(46, '000000', 'Arial', 'Crowd begins to sing', 2),
(47, '000000', 'Arial', 'Crowd being mentioned by the Announcers', 2),
(48, '000000', 'Arial', 'Shot off the crossbar', 2),
(49, '000000', 'Arial', 'Announcer brings up the weather', 2),
(50, '000000', 'Arial', 'Pass Intercepted', 2),
(51, '000000', 'Arial', 'Duel', 3),
(52, '000000', 'Arial', 'Vote', 3),
(53, '000000', 'Arial', 'Rise up', 3),
(54, '000000', 'Arial', 'King George', 3),
(55, '000000', 'Arial', 'Maria Reynolds', 3),
(56, '000000', 'Arial', 'Eliza Schuyler', 3),
(57, '000000', 'Arial', 'Richard Rodgers Threater', 3),
(58, '000000', 'Arial', 'Aaron Burr', 3),
(59, '000000', 'Arial', 'I am not throwing away my shot', 3),
(60, '000000', 'Arial', 'George Washington', 3),
(61, '000000', 'Arial', 'Alexander Hamilton', 3),
(62, '000000', 'Arial', 'James Madison', 3),
(63, '000000', 'Arial', 'Hurricane', 3),
(64, '000000', 'Arial', 'In the room where it happened', 3),
(65, '000000', 'Arial', 'Vanessa', 3),
(66, '000000', 'Arial', 'Phillip Hamiltion', 3),
(67, '000000', 'Arial', 'Ron Chernow', 3),
(68, '000000', 'Arial', 'Leslie Odom Jr', 3),
(69, '000000', 'Arial', 'Weehawken', 3),
(70, '000000', 'Arial', 'Look around at how lucky we are to be alive right now', 3),
(71, '000000', 'Arial', 'Someone counts', 3),
(72, '000000', 'Arial', 'Revolution', 3),
(73, '000000', 'Arial', 'Marquis de Lafayette', 3),
(74, '000000', 'Arial', 'Rise Up', 3),
(75, '000000', 'Arial', 'Singing about war', 3),
(76, '000000', 'Arial', 'Cabbage Man', 4),
(77, '000000', 'Arial', 'Earth Bending', 4),
(78, '000000', 'Arial', 'Water Bending', 4),
(79, '000000', 'Arial', 'Fire Bending', 4),
(80, '000000', 'Arial', 'Metal Bending', 4),
(81, '000000', 'Arial', 'Lightning bending', 4),
(82, '000000', 'Arial', '\"Yip Yip\"', 4),
(83, '000000', 'Arial', 'Someone mocks Zuko', 4),
(84, '000000', 'Arial', 'Sokka yells what attack he is using', 4),
(85, '000000', 'Arial', 'Sokka talks about meat', 4),
(86, '000000', 'Arial', 'Iroh mentions tea', 4),
(87, '000000', 'Arial', 'Toph being blind', 4),
(88, '000000', 'Arial', 'Zuko talks about honor', 4),
(89, '000000', 'Arial', 'Someone puts on a disguise', 4),
(90, '000000', 'Arial', 'Flashback', 4),
(91, '000000', 'Arial', 'Zuko loses his temper', 4),
(92, '000000', 'Arial', 'Gullible fire nation soldier', 4),
(93, '000000', 'Arial', 'All four elements used in a single episode', 4),
(94, '000000', 'Arial', 'Iroh gives advice', 4),
(95, '000000', 'Arial', 'Toph insults someone', 4),
(96, '000000', 'Arial', 'Zoom in on Zukos eyes', 4),
(97, '000000', 'Arial', 'Sokka refers to bending by anything but bending', 4),
(98, '000000', 'Arial', 'White Lotus', 4),
(99, '000000', 'Arial', 'Twinkletoes', 4),
(100, '000000', 'Arial', 'MOMO', 4),
(101, '000000', 'Arial', 'Someone says \"Capture the Avatar\"', 4),
(102, '000000', 'Arial', 'Zoku switches sides (again?)', 4),
(103, '000000', 'Arial', 'Roku is brought up or shown', 4),
(104, '000000', 'Arial', '\"That\'s rough buddy\"', 4),
(105, '000000', 'Arial', 'Ty Lee paralyzed someone/limb', 4),
(106, '000000', 'Arial', 'Mai being depressed', 4),
(107, '000000', 'Arial', 'Avatar state is used', 4),
(108, '000000', 'Arial', '1', 5),
(109, '000000', 'Arial', '2', 5),
(110, '000000', 'Arial', '3', 5),
(111, '000000', 'Arial', '4', 5),
(112, '000000', 'Arial', '5', 5),
(113, '000000', 'Arial', '6', 5),
(114, '000000', 'Arial', '7', 5),
(115, '000000', 'Arial', '8', 5),
(116, '000000', 'Arial', '9', 5),
(117, '000000', 'Arial', '10', 5),
(118, '000000', 'Arial', '11', 5),
(119, '000000', 'Arial', '12', 5),
(120, '000000', 'Arial', '13', 5),
(121, '000000', 'Arial', '14', 5),
(122, '000000', 'Arial', '15', 5),
(123, '000000', 'Arial', '16', 5),
(124, '000000', 'Arial', '17', 5),
(125, '000000', 'Arial', '18', 5),
(126, '000000', 'Arial', '19', 5),
(127, '000000', 'Arial', '20', 5),
(128, '000000', 'Arial', '21', 5),
(129, '000000', 'Arial', '22', 5),
(130, '000000', 'Arial', '23', 5),
(131, '000000', 'Arial', '24', 5),
(132, '000000', 'Arial', '25', 5),
(133, '000000', 'Arial', '26', 5),
(134, '000000', 'Arial', '27', 5),
(135, '000000', 'Arial', '28', 5),
(136, '000000', 'Arial', '29', 5),
(137, '000000', 'Arial', '30', 5),
(138, '000000', 'Arial', '31', 5),
(139, '000000', 'Arial', '32', 5),
(140, '000000', 'Arial', '33', 5),
(141, '000000', 'Arial', '34', 5),
(142, '000000', 'Arial', '35', 5),
(143, '000000', 'Arial', '36', 5),
(144, '000000', 'Arial', '37', 5),
(145, '000000', 'Arial', '38', 5),
(146, '000000', 'Arial', '39', 5),
(147, '000000', 'Arial', '40', 5),
(148, '000000', 'Arial', '41', 5),
(149, '000000', 'Arial', '42', 5),
(150, '000000', 'Arial', '43', 5),
(151, '000000', 'Arial', '44', 5),
(152, '000000', 'Arial', '45', 5),
(153, '000000', 'Arial', '46', 5),
(154, '000000', 'Arial', '47', 5),
(155, '000000', 'Arial', '48', 5),
(156, '000000', 'Arial', '49', 5),
(157, '000000', 'Arial', '50', 5),
(158, '000000', 'Arial', '51', 5),
(159, '000000', 'Arial', '52', 5),
(160, '000000', 'Arial', '53', 5),
(161, '000000', 'Arial', '54', 5),
(162, '000000', 'Arial', '55', 5),
(163, '000000', 'Arial', '56', 5),
(164, '000000', 'Arial', '57', 5),
(165, '000000', 'Arial', '58', 5),
(166, '000000', 'Arial', '59', 5),
(167, '000000', 'Arial', '60', 5),
(168, '000000', 'Arial', '61', 5),
(169, '000000', 'Arial', '62', 5),
(170, '000000', 'Arial', '63', 5),
(171, '000000', 'Arial', '64', 5),
(172, '000000', 'Arial', '65', 5),
(173, '000000', 'Arial', '66', 5),
(174, '000000', 'Arial', '67', 5),
(175, '000000', 'Arial', '68', 5),
(176, '000000', 'Arial', '69', 5),
(177, '000000', 'Arial', '70', 5),
(178, '000000', 'Arial', '71', 5),
(179, '000000', 'Arial', '72', 5),
(180, '000000', 'Arial', '73', 5),
(181, '000000', 'Arial', '74', 5),
(182, '000000', 'Arial', '75', 5),
(183, '000000', 'Arial', '76', 5),
(184, '000000', 'Arial', '77', 5),
(185, '000000', 'Arial', '78', 5),
(186, '000000', 'Arial', '79', 5),
(187, '000000', 'Arial', '80', 5),
(188, '000000', 'Arial', '81', 5),
(189, '000000', 'Arial', '82', 5),
(190, '000000', 'Arial', '83', 5),
(191, '000000', 'Arial', '84', 5),
(192, '000000', 'Arial', '85', 5),
(193, '000000', 'Arial', '86', 5),
(194, '000000', 'Arial', '87', 5),
(195, '000000', 'Arial', '88', 5),
(196, '000000', 'Arial', '89', 5),
(197, '000000', 'Arial', '90', 5),
(198, '000000', 'Arial', '91', 5),
(199, '000000', 'Arial', '92', 5),
(200, '000000', 'Arial', '93', 5),
(201, '000000', 'Arial', '94', 5),
(202, '000000', 'Arial', '95', 5),
(203, '000000', 'Arial', '96', 5),
(204, '000000', 'Arial', '97', 5),
(205, '000000', 'Arial', '98', 5),
(206, '000000', 'Arial', '99', 5),
(207, '000000', 'Arial', '100', 5);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `type` varchar(31) NOT NULL,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`type`, `user_id`, `user_name`, `password`) VALUES
('Registered', 1, 'Ryan', '$2a$10$jPow9DkDV8ECXEpXs3U9UuxCXFfOXeVL3JYI/SfRGyu9jv4R61QHK'),
('Registered', 2, 'Devon', '$2a$10$u5OeKs2I/thULRRsZPlxMuMjhXyItE7szZvhLZC/O5i7ACyoVTV8u'),
('Guest', 3, 'Alyssa', NULL),
('Guest', 4, 'James', NULL),
('Guest', 5, 'Ryan', NULL),
('Guest', 52, 'Test', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users_seq`
--

CREATE TABLE `users_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users_seq`
--

INSERT INTO `users_seq` (`next_val`) VALUES
(151);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bingo_mixes`
--
ALTER TABLE `bingo_mixes`
  ADD PRIMARY KEY (`mix_id`),
  ADD KEY `FKewa3382tdvc7uycfwvx3yv9sr` (`owner_id`);

--
-- Indexes for table `games`
--
ALTER TABLE `games`
  ADD PRIMARY KEY (`game_id`),
  ADD KEY `FK5tly2ihx4t84to6lfb4qrjo1b` (`owner_id`),
  ADD KEY `FKebyl4ba5ysidxi39ohboto42` (`settings_id`),
  ADD KEY `FKb7rxqdrxqhliqc8xqn2hub9xq` (`winner_id`);

--
-- Indexes for table `game_settings`
--
ALTER TABLE `game_settings`
  ADD PRIMARY KEY (`setting_id`),
  ADD KEY `FK9rujyweea29qs9plg5wgoh1k9` (`mix_id`);

--
-- Indexes for table `phrases`
--
ALTER TABLE `phrases`
  ADD PRIMARY KEY (`pharse_id`),
  ADD KEY `FKhg5x4yr6nxp9m6j9mvc3ewdy0` (`mix_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `phrases`
--
ALTER TABLE `phrases`
  MODIFY `pharse_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=208;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
