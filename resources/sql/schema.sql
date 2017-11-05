CREATE DATABASE  IF NOT EXISTS `smart_portables` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `smart_portables`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: smart_portables
-- ------------------------------------------------------
-- Server version	5.7.20-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` varchar(20) NOT NULL,
  `name` varchar(20) DEFAULT NULL,
  `image-overview` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login_user`
--

DROP TABLE IF EXISTS `login_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `login_user` (
  `seq_no` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`seq_no`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login_user`
--

LOCK TABLES `login_user` WRITE;
/*!40000 ALTER TABLE `login_user` DISABLE KEYS */;
INSERT INTO `login_user` VALUES (1,'admin','admin','store_manager'),(2,'sale','sale','sales'),(3,'customer','cust','customer'),(4,'customer_2','customer','customer');
/*!40000 ALTER TABLE `login_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order` (
  `seq_no` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) DEFAULT NULL,
  `order_date` date DEFAULT NULL,
  `deliver_date` date DEFAULT NULL,
  `confirm_number` bigint(10) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `zip` int(11) DEFAULT NULL,
  `phone` bigint(10) DEFAULT NULL,
  `credit_card` bigint(18) DEFAULT NULL,
  `expire` char(5) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `product_link` mediumtext,
  PRIMARY KEY (`seq_no`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (2,3,'2017-10-19','2017-11-02',3578692,'Tai','1234','Chicago','IL',60616,2345678910,2344446678991645,'05/18','Delivered','laptop,1,1;headphone,1,1;accessory,1,1;'),(3,1,'2017-10-19','2017-11-02',6765706,'Tai','1234','Chicago','IL',60616,12345678910,2344446678991645,'05/18','Placed','smartwatch,3,3;'),(4,4,'2017-10-19','2017-11-02',3915558,'Tai','1234','Chicago','IL',60616,12345678910,2344446678991645,'05/18','Placed','phone,1,1;phone,2,5;'),(5,1,'2017-10-25','2017-11-08',3649704,'Tai','1234','Chicago','IL',60616,12345678910,2344446678991645,'05/18','Placed','laptop,8,5;'),(6,1,'2017-10-25','2017-11-08',1153416,'Tai','1234','Chicago','IL',60616,12345678910,2344446678991645,'05/18','Placed','laptop,2,5;'),(7,1,'2017-10-25','2017-11-08',908118,'Tai','1234','Chicago','IL',60616,12345678910,2344446678991645,'05/18','Placed','laptop,8,2;'),(8,1,'2017-10-25','2017-11-08',9415285,'Tai','1234','Chicago','IL',60616,12345678910,2344446678991645,'05/18','Placed','laptop,8,5;'),(9,1,'2017-10-29','2017-11-12',6464949,'Tai','1234','Chicago','IL',60616,12345678910,2344446678991645,'05/18','Placed','laptop,8,2;accessory,4,1;accessory,5,2;laptop,1,1;');
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `seq_no` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `image` varchar(100) DEFAULT NULL,
  `price` decimal(13,2) DEFAULT NULL,
  `discount` decimal(13,2) DEFAULT NULL,
  `rebate` decimal(13,2) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`seq_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_accessories`
--

DROP TABLE IF EXISTS `product_accessories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_accessories` (
  `product` int(11) NOT NULL,
  `accessories` int(11) DEFAULT NULL,
  PRIMARY KEY (`product`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_accessories`
--

LOCK TABLES `product_accessories` WRITE;
/*!40000 ALTER TABLE `product_accessories` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_accessories` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-04 20:36:58
