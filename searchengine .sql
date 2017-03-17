-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 17, 2017 at 11:29 PM
-- Server version: 5.7.14
-- PHP Version: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `searchengine`
--

-- --------------------------------------------------------

--
-- Table structure for table `record`
--

CREATE TABLE `record` (
  `ID` int(11) NOT NULL,
  `URL` text NOT NULL,
  `document` text NOT NULL,
  `Visted` tinyint(1) NOT NULL,
  `file` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `record`
--

INSERT INTO `record` (`ID`, `URL`, `document`, `Visted`, `file`) VALUES
(1, 'https://en.wikipedia.org/wiki/Main_Page', '', 1, 1),
(2, 'http://stackoverflow.com/', '', 1, 1),
(3, 'http://www.dmoz.org/', 'DMOZ - The Directory of the Web DMOZ #OrganizeTheWeb Important Notice As of Mar 14, 2017 dmoz.org will no longer be available. Visit resource-zone to stay in touch with the community. Follow @dmoz About Become an Editor Suggest a Site Help Login Share via Facebook Share via Twitter Share via LinkedIn Share via e-Mail Visit DMOZ on Facebook Visit DMOZ on Twitter Help with Search Arts Movies, Television, Music... Business Jobs, Real Estate, Investing... Computers Internet, Software, Hardware... Games Video Games, RPGs, Gambling... Health Fitness, Medicine, Alternative... Home Family, Consumers, Cooking... News Media, Newspapers, Weather... Recreation Travel, Food, Outdoors, Humor... Reference Maps, Education, Libraries... Regional US, Canada, UK, Europe... Science Biology, Psychology, Physics... Shopping Clothing, Food, Gifts... Society People, Religion, Issues... Sports Baseball, Soccer, Basketball... Kids & Teens Directory Arts, School Time, Teen Life... DMOZ around the World Deutsch, Français, ???, Italiano, Español, ???????, Nederlands, Polski, Türkçe, Dansk, ????, ... 91,930 Editors 1,031,708 Categories 3,861,329 Sites 90 Languages Copyright © 1998-2017 AOL Inc. Terms of Use CMBuild 3.0.4-819168 Tue Jan 10 15:40:44 EST 2017', 1, 1),
(5, 'http://unicef.org/', 'Home | UNICEF Skip to main content English Français Español ??????? ?? English Français Español ??????? ?? Explore UNICEF About UNICEF Where we work Work for UNICEF UNICEF Connect Voices of Youth UNICEF data Support UNICEF Press centre Donate Menu Search Main navigation What we do Research and Reports Where we work Take Action Search Close English Français Español ??????? ?? English Français Español ??????? ?? Main navigation What we do Research and Reports Where we work Take Action Search Close Press centre Explore UNICEF About UNICEF Where we work Work for UNICEF UNICEF Connect Voices of Youth UNICEF data Support UNICEF Donate Search for every child, hope hope a safe home laughter an education play a future love clean water childhood a voice a fair chance Across 190 countries and territories, UNICEF defends the rights of children UNICEF’s work Discover the ways that UNICEF advocates for the rights of children around the globe to protect the lives of every child, every day. Child protection and inclusion Child survival Education UNICEF in emergencies Gender Innovation for children Supply and logistics Research and analysis Explore what we do Take action Children need champions. Get involved, speak out, volunteer, or become a donor and give every child a fair chance to succeed. Join UNICEF Search UNICEF Search UNICEF home What we do Research and reports Where we work Press centre About us UNICEF Executive Board Work for UNICEF Partner with UNICEF Take action Related UNICEF sites Related UNICEF sites UNICEF Connect Voices of Youth UNICEF data Support UNICEF Become a donor RSS Twitter Facebook Tumblr Youtube Legal Contact us You can change a child’s life Select your country, and we’ll direct you to your local donation page. Your country Go to local donation page No thanks', 1, 1),
(6, 'http://nature.com/index.html', 'Home | UNICEF Skip to main content English Français Español ??????? ?? English Français Español ??????? ?? Explore UNICEF About UNICEF Where we work Work for UNICEF UNICEF Connect Voices of Youth UNICEF data Support UNICEF Press centre Donate Menu Search Main navigation What we do Research and Reports Where we work Take Action Search Close English Français Español ??????? ?? English Français Español ??????? ?? Main navigation What we do Research and Reports Where we work Take Action Search Close Press centre Explore UNICEF About UNICEF Where we work Work for UNICEF UNICEF Connect Voices of Youth UNICEF data Support UNICEF Donate Search for every child, hope hope a safe home laughter an education play a future love clean water childhood a voice a fair chance Across 190 countries and territories, UNICEF defends the rights of children UNICEF’s work Discover the ways that UNICEF advocates for the rights of children around the globe to protect the lives of every child, every day. Child protection and inclusion Child survival Education UNICEF in emergencies Gender Innovation for children Supply and logistics Research and analysis Explore what we do Take action Children need champions. Get involved, speak out, volunteer, or become a donor and give every child a fair chance to succeed. Join UNICEF Search UNICEF Search UNICEF home What we do Research and reports Where we work Press centre About us UNICEF Executive Board Work for UNICEF Partner with UNICEF Take action Related UNICEF sites Related UNICEF sites UNICEF Connect Voices of Youth UNICEF data Support UNICEF Become a donor RSS Twitter Facebook Tumblr Youtube Legal Contact us You can change a child’s life Select your country, and we’ll direct you to your local donation page. Your country Go to local donation page No thanks', 1, 1),
(8, 'https://en.wikipedia.org/wiki/Main_Page', '', 1, 1),
(15, 'http://www.dmoz.org/docs/en/about.html', '', 1, 1),
(16, 'https://twitter.com/dmoz', '', 0, 0),
(17, 'http://www.dmoz.org/docs/en/help/become.html', '', 0, 0),
(18, 'http://www.dmoz.org/docs/en/add.html', '', 0, 0),
(19, 'https://www.unicef.org/#main-content', '', 0, 0),
(20, 'http://www.dmoz.org/docs/en/help/helpmain.html', '', 0, 0),
(21, 'http://www.dmoz.org/editors/', '', 0, 0),
(22, 'http://www.facebook.com/dmoz', '', 0, 0),
(23, 'https://www.unicef.org/', '', 0, 0),
(24, 'http://www.twitter.com/dmoz', '', 0, 0),
(25, 'http://www.dmoz.org/docs/en/searchguide.html', '', 0, 0),
(26, 'http://www.dmoz.org/Arts/', '', 0, 0),
(27, 'http://www.dmoz.org/Arts/Movies/', '', 0, 0),
(28, 'http://www.dmoz.org/Arts/Television/', '', 0, 0),
(29, 'https://www.unicef.org/fr', '', 0, 0),
(30, 'http://www.dmoz.org/Arts/Music/', '', 0, 0),
(31, 'http://www.dmoz.org/Business/', '', 0, 0),
(32, 'https://www.unicef.org/es', '', 0, 0),
(33, 'http://www.dmoz.org/Business/Employment/', '', 0, 0),
(34, 'http://www.dmoz.org/Business/Real_Estate/', '', 0, 0),
(35, 'https://www.unicef.org/ar', '', 0, 0),
(36, 'http://www.dmoz.org/Business/Investing/', '', 0, 0),
(37, 'http://www.dmoz.org/Computers/', '', 0, 0),
(38, 'https://www.unicef.org/zh', '', 0, 0),
(39, 'http://www.dmoz.org/Computers/Internet/', '', 0, 0),
(40, 'https://www.unicef.org/about-us', '', 0, 0),
(41, 'http://www.dmoz.org/Computers/Software/', '', 0, 0),
(42, 'http://www.dmoz.org/Computers/Hardware/', '', 0, 0),
(43, 'https://www.unicef.org/where-we-work', '', 0, 0),
(44, 'http://www.unicef.org/about/employ/index_careers.html', '', 0, 0),
(45, 'http://www.dmoz.org/Games/', '', 0, 0),
(46, 'http://www.dmoz.org/Games/Video_Games/', '', 0, 0),
(47, 'http://www.dmoz.org/Games/Roleplaying/', '', 0, 0),
(48, 'https://blogs.unicef.org/', '', 0, 0),
(49, 'http://www.voicesofyouth.org/', '', 0, 0),
(50, 'https://data.unicef.org', '', 0, 0),
(51, 'http://www.dmoz.org/Games/Gambling/', '', 0, 0),
(52, 'http://www.dmoz.org/Health/', '', 0, 0),
(53, 'http://support.unicef.org/', '', 0, 0),
(54, 'http://www.unicef.org/media/', '', 0, 0),
(55, 'https://www.unicef.org/what-we-do', '', 0, 0),
(56, 'https://www.unicef.org/reports', '', 0, 0),
(57, 'https://www.unicef.org/take-action', '', 0, 0),
(58, 'http://www.dmoz.org/Health/Fitness/', '', 0, 0),
(59, 'http://www.dmoz.org/Health/Medicine/', '', 0, 0),
(60, 'http://www.dmoz.org/Health/Alternative/', '', 0, 0),
(61, 'http://www.dmoz.org/Home/', '', 0, 0),
(62, 'http://www.dmoz.org/Home/Family/', '', 0, 0),
(63, 'http://www.dmoz.org/Home/Consumer_Information/', '', 0, 0),
(64, 'http://www.dmoz.org/Home/Cooking/', '', 0, 0),
(65, 'http://www.dmoz.org/News/', '', 0, 0),
(66, 'http://www.dmoz.org/News/Media/', '', 0, 0),
(67, 'http://www.dmoz.org/News/Newspapers/', '', 0, 0),
(68, 'http://www.dmoz.org/News/Weather/', '', 0, 0),
(69, 'http://www.dmoz.org/Recreation/', '', 0, 0),
(70, 'http://www.dmoz.org/Recreation/Travel/', '', 0, 0),
(71, 'http://www.dmoz.org/Recreation/Food/', '', 0, 0),
(72, 'http://www.dmoz.org/Recreation/Outdoors/', '', 0, 0),
(73, 'http://www.dmoz.org/Recreation/Humor/', '', 0, 0),
(74, 'http://www.dmoz.org/Reference/', '', 0, 0),
(75, 'http://www.dmoz.org/Reference/Maps/', '', 0, 0),
(76, 'http://www.dmoz.org/Reference/Education/', '', 0, 0),
(77, 'http://www.dmoz.org/Reference/Libraries/', '', 0, 0),
(78, 'http://www.dmoz.org/Regional/', '', 0, 0),
(79, 'https://www.unicef.org/what-we-do#child-protection', '', 0, 0),
(80, 'http://www.dmoz.org/Regional/North_America/United_States/', '', 0, 0),
(81, 'http://www.dmoz.org/Regional/North_America/Canada/', '', 0, 0),
(82, 'http://www.dmoz.org/Regional/Europe/United_Kingdom/', '', 0, 0),
(83, 'http://www.dmoz.org/Regional/Europe/', '', 0, 0),
(84, 'http://www.dmoz.org/Science/', '', 0, 0),
(85, 'http://www.dmoz.org/Science/Biology/', '', 0, 0),
(86, 'http://www.dmoz.org/Science/Social_Sciences/Psychology/', '', 0, 0),
(87, 'http://www.dmoz.org/Science/Physics/', '', 0, 0),
(88, 'https://www.unicef.org/what-we-do#child-survival', '', 0, 0),
(89, 'http://www.dmoz.org/Shopping/', '', 0, 0),
(90, 'https://www.unicef.org/what-we-do#education', '', 0, 0),
(91, 'http://www.dmoz.org/Shopping/Clothing/', '', 0, 0),
(92, 'https://www.unicef.org/what-we-do#unicef-emergencies', '', 0, 0),
(93, 'http://www.dmoz.org/Shopping/Food/', '', 0, 0),
(94, 'https://www.unicef.org/what-we-do#gender', '', 0, 0),
(95, 'https://www.unicef.org/what-we-do#innovation-for-children', '', 0, 0),
(96, 'http://www.dmoz.org/Shopping/Gifts/', '', 0, 0),
(97, 'https://www.unicef.org/what-we-do#supply-and-logistics', '', 0, 0),
(98, 'http://www.dmoz.org/Society/', '', 0, 0),
(99, 'https://www.unicef.org/what-we-do#research-and-analysis', '', 0, 0),
(100, 'http://www.dmoz.org/Society/People/', '', 0, 0),
(101, 'http://www.dmoz.org/Society/Religion_and_Spirituality/', '', 0, 0),
(102, 'http://www.dmoz.org/Society/Issues/', '', 0, 0),
(103, 'http://www.dmoz.org/Sports/', '', 0, 0),
(104, 'http://www.dmoz.org/Sports/Baseball/', '', 0, 0),
(105, 'http://www.dmoz.org/Sports/Soccer/', '', 0, 0),
(106, 'http://www.dmoz.org/Sports/Basketball/', '', 0, 0),
(107, 'http://www.dmoz.org/Kids_and_Teens/', '', 0, 0),
(108, 'http://www.dmoz.org/Kids_and_Teens/Arts/', '', 0, 0),
(109, 'http://www.dmoz.org/Kids_and_Teens/School_Time/', '', 0, 0),
(110, 'http://www.dmoz.org/Kids_and_Teens/Teen_Life/', '', 0, 0),
(111, 'https://www.unicef.org/about/execboard/', '', 0, 0),
(112, 'http://www.unicef.org/corporate_partners/index_33855.html', '', 0, 0),
(113, 'http://www.dmoz.org/World/', '', 0, 0),
(114, 'http://www.dmoz.org/World/Deutsch/', '', 0, 0),
(115, 'https://data.unicef.org/', '', 0, 0),
(116, 'http://www.dmoz.org/World/Fran%C3%A7ais/', '', 0, 0),
(117, 'http://www.unicef.org/rss/unicef_news.xml', '', 0, 0),
(118, 'http://www.dmoz.org/World/Japanese/', '', 0, 0),
(119, 'https://twitter.com/unicef', '', 0, 0),
(120, 'http://www.dmoz.org/World/Italiano/', '', 0, 0),
(121, 'https://www.facebook.com/unicef', '', 0, 0),
(122, 'http://www.dmoz.org/World/Espa%C3%B1ol/', '', 0, 0),
(123, 'http://www.dmoz.org/World/Russian/', '', 0, 0),
(124, 'http://unicef.tumblr.com/', '', 0, 0),
(125, 'http://www.dmoz.org/World/Nederlands/', '', 0, 0),
(126, 'http://www.dmoz.org/World/Polski/', '', 0, 0),
(127, 'https://www.youtube.com/unicef', '', 0, 0),
(128, 'http://www.dmoz.org/World/T%C3%BCrk%C3%A7e/', '', 0, 0),
(129, 'http://www.unicef.org/about/legal.html', '', 0, 0),
(130, 'http://www.unicef.org/about/contact.html', '', 0, 0),
(131, 'http://www.dmoz.org/World/Dansk/', '', 0, 0),
(132, 'https://www.unicef.org/#', '', 0, 0),
(133, 'http://www.dmoz.org/World/Chinese_Simplified/', '', 0, 0),
(134, 'http://www.dmoz.org/docs/en/termsofuse.html', '', 0, 0),
(135, 'http://www.dmoz.org/docs/en/cmbuild.html', '', 0, 0),
(136, 'https://goo.gl/forms/i14zX8LIdPBsCCst1', '', 0, 0),
(137, 'http://www.nature.com/#content', '', 0, 0),
(138, 'http://www.dmoz.org/about.html', '', 0, 0),
(139, 'http://www.nature.com/#menu', '', 0, 0),
(140, 'http://www.nature.com/', '', 0, 0),
(141, 'http://www.nature.com/#search-menu', '', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `restrictedurls`
--

CREATE TABLE `restrictedurls` (
  `ulr` text NOT NULL,
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `restrictedurls`
--

INSERT INTO `restrictedurls` (`ulr`, `id`) VALUES
('https://blogs.unicef.org//wp-admin/', 1),
('http://www.nature.com//laban/', 2),
('http://www.nature.com//laban', 3),
('http://www.nature.com//*.pdf$', 4),
('http://www.nature.com//*/*/*/pf/', 5),
('http://www.nature.com//*.otmi$', 6),
('http://www.nature.com//*/*/*/*/otmi/', 7),
('http://www.nature.com//*/*/*/*/fp/', 8),
('http://www.nature.com//protocolexchange/labgroups/', 9);

-- --------------------------------------------------------

--
-- Table structure for table `word`
--

CREATE TABLE `word` (
  `word` text NOT NULL,
  `Wid` bigint(20) NOT NULL,
  `Stemming` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `word`
--

INSERT INTO `word` (`word`, `Wid`, `Stemming`) VALUES
('to get a', 3975, NULL),
('of the', 3976, NULL),
('as an', 3977, NULL),
('then the', 3978, NULL),
('then a', 3979, NULL),
('in the', 3980, NULL),
('by the', 3981, NULL),
('will be', 3982, NULL),
('are all the', 3983, NULL),
('of this', 3984, NULL),
('comput', 3985, NULL),
('computing', 3986, 3985),
('computer', 3987, 3985),
('send', 3988, NULL),
('java', 3989, NULL),
('tostr', 3990, NULL),
('tostring', 3991, 3990),
('method', 3992, NULL),
('job', 3993, NULL),
('jobs', 3994, 3993),
('sendfil', 3995, NULL),
('sendfiles', 3996, 3995),
('whiteboard', 3997, NULL),
('net', 3998, NULL),
('meet', 3999, NULL),
('meeting', 4000, 3999),
('tool', 4001, NULL),
('tools', 4002, 4001),
('articl', 4003, NULL),
('articles', 4004, 4003),
('facebook', 4005, NULL),
('googl', 4006, NULL),
('google', 4007, 4006),
('twitter', 4008, NULL),
('linkedin', 4009, NULL),
('youtub', 4010, NULL),
('youtube', 4011, 4010),
('home', 4012, NULL),
('tutori', 4013, NULL),
('tutorials', 4014, 4013),
('librari', 4015, NULL),
('library', 4016, 4015),
('code', 4017, NULL),
('coding', 4018, 4017),
('ground', 4019, NULL),
('tutor', 4020, NULL),
('connect', 4021, NULL),
('video', 4022, NULL),
('videos', 4023, 4022),
('search', 4024, NULL),
('tutorial', 4025, 4013),
('overview', 4026, NULL),
('environ', 4027, NULL),
('environment', 4028, 4027),
('setup', 4029, NULL),
('basic', 4030, NULL),
('syntax', 4031, NULL),
('object', 4032, NULL),
('class', 4033, NULL),
('classes', 4034, 4033),
('datatyp', 4035, NULL),
('datatypes', 4036, 4035),
('variabl', 4037, NULL),
('variable', 4038, 4037),
('type', 4039, NULL),
('types', 4040, 4039),
('modifi', 4041, NULL),
('modifier', 4042, 4041),
('oper', 4043, NULL),
('operators', 4044, 4043),
('loop', 4045, NULL),
('control', 4046, NULL),
('decis', 4047, NULL),
('decision', 4048, 4047),
('make', 4049, NULL),
('making', 4050, 4049),
('number', 4051, NULL),
('numbers', 4052, 4051),
('charact', 4053, NULL),
('characters', 4054, 4053),
('string', 4055, NULL),
('strings', 4056, 4055),
('arrai', 4057, NULL),
('arrays', 4058, 4057),
('date', 4059, NULL),
('time', 4060, NULL),
('regular', 4061, NULL),
('express', 4062, NULL),
('expressions', 4063, 4062),
('methods', 4064, 3992),
('file', 4065, NULL),
('files', 4066, 4065),
('o', 4067, NULL),
('except', 4068, NULL),
('exceptions', 4069, 4068),
('inner', 4070, NULL),
('orient', 4071, NULL),
('oriented', 4072, 4071),
('inherit', 4073, NULL),
('inheritance', 4074, 4073),
('overrid', 4075, NULL),
('overriding', 4076, 4075),
('polymorph', 4077, NULL),
('polymorphism', 4078, 4077),
('abstract', 4079, NULL),
('abstraction', 4080, 4079),
('encapsul', 4081, NULL),
('encapsulation', 4082, 4081),
('interfac', 4083, NULL),
('interfaces', 4084, 4083),
('packag', 4085, NULL),
('packages', 4086, 4085),
('advanc', 4087, NULL),
('advanced', 4088, 4087),
('data', 4089, NULL),
('structur', 4090, NULL),
('structures', 4091, 4090),
('collect', 4092, NULL),
('collections', 4093, 4092),
('gener', 4094, NULL),
('generics', 4095, 4094),
('serial', 4096, NULL),
('serialization', 4097, 4096),
('network', 4098, NULL),
('networking', 4099, 4098),
('sending', 4100, 3988),
('email', 4101, NULL),
('multithread', 4102, NULL),
('multithreading', 4103, 4102),
('applet', 4104, NULL),
('basics', 4105, 4030),
('document', 4106, NULL),
('documentation', 4107, 4106),
('us', 4108, NULL),
('useful', 4109, 4108),
('resourc', 4110, NULL),
('resources', 4111, 4110),
('question', 4112, NULL),
('questions', 4113, 4112),
('answer', 4114, NULL),
('answers', 4115, 4114),
('quick', 4116, NULL),
('guid', 4117, NULL),
('guide', 4118, 4117),
('discuss', 4119, NULL),
('discussion', 4120, 4119),
('exampl', 4121, NULL),
('examples', 4122, 4121),
('select', 4123, NULL),
('selected', 4124, 4123),
('read', 4125, NULL),
('reading', 4126, 4125),
('develop', 4127, NULL),
('developer', 4128, 4127),
('best', 4129, NULL),
('practic', 4130, NULL),
('practices', 4131, 4130),
('effect', 4132, NULL),
('effective', 4133, 4132),
('resum', 4134, NULL),
('resume', 4135, 4134),
('write', 4136, NULL),
('writing', 4137, 4136),
('hr', 4138, NULL),
('interview', 4139, NULL),
('glossari', 4140, NULL),
('glossary', 4141, 4140),
('advertis', 4142, NULL),
('advertisements', 4143, 4142),
('previou', 4144, NULL),
('previous', 4145, 4144),
('page', 4146, NULL),
('next', 4147, NULL),
('descript', 4148, NULL),
('description', 4149, 4148),
('used', 4150, 4108),
('repres', 4151, NULL),
('representing', 4152, 4151),
('valu', 4153, NULL),
('value', 4154, 4153),
('take', 4155, NULL),
('takes', 4156, 4155),
('primit', 4157, NULL),
('primitive', 4158, 4157),
('argument', 4159, NULL),
('return', 4160, NULL),
('returned', 4161, 4160),
('two', 4162, NULL),
('arguments', 4163, 4159),
('represent', 4164, NULL),
('representation', 4165, 4164),
('first', 4166, NULL),
('radix', 4167, NULL),
('specifi', 4168, NULL),
('specified', 4169, 4168),
('second', 4170, NULL),
('follow', 4171, NULL),
('following', 4172, 4171),
('variant', 4173, NULL),
('variants', 4174, 4173);

-- --------------------------------------------------------

--
-- Table structure for table `wordpositions`
--

CREATE TABLE `wordpositions` (
  `w_id` bigint(20) NOT NULL,
  `doc_id` int(11) NOT NULL,
  `position` int(11) NOT NULL,
  `Importance` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `record`
--
ALTER TABLE `record`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `restrictedurls`
--
ALTER TABLE `restrictedurls`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `word`
--
ALTER TABLE `word`
  ADD PRIMARY KEY (`Wid`);

--
-- Indexes for table `wordpositions`
--
ALTER TABLE `wordpositions`
  ADD PRIMARY KEY (`w_id`,`doc_id`,`position`),
  ADD KEY `doc_id` (`doc_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `record`
--
ALTER TABLE `record`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=142;
--
-- AUTO_INCREMENT for table `restrictedurls`
--
ALTER TABLE `restrictedurls`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `word`
--
ALTER TABLE `word`
  MODIFY `Wid` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4175;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `wordpositions`
--
ALTER TABLE `wordpositions`
  ADD CONSTRAINT `wordpositions_ibfk_1` FOREIGN KEY (`doc_id`) REFERENCES `record` (`ID`),
  ADD CONSTRAINT `wordpositions_ibfk_2` FOREIGN KEY (`w_id`) REFERENCES `word` (`Wid`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
