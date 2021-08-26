
 SET NAMES utf8 ;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS comment;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;



# 评论表
CREATE TABLE comment(
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) DEFAULT NULL  COMMENT '用户id,对应是哪个用户发布的',
  entity_type int(11) DEFAULT NULL COMMENT '评论的目标类型 1:帖子 2:评论',
  entity_id int(11) DEFAULT NULL COMMENT '类型的目标id 比如 类型为帖子的id:228',
  target_id int(11) DEFAULT NULL  COMMENT '评论指向的目标id ==>某个人',
  content text,
  status int(11) DEFAULT NULL  COMMENT '评论的状态 0:正常 1:不可用',
  create_time timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`) /*!80000 INVISIBLE */,
  KEY `index_entity_id` (`entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `discuss_post`
--

DROP TABLE IF EXISTS `discuss_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;


# 首页的帖子表
CREATE TABLE `discuss_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(45) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `content` text,
  `type` int(11) DEFAULT NULL COMMENT '0-普通; 1-置顶;',
  `status` int(11) DEFAULT NULL COMMENT '0-正常; 1-精华; 2-拉黑;',
  `create_time` timestamp NULL DEFAULT NULL,
  `comment_count` int(11) DEFAULT NULL COMMENT '评论数量;这样做的目的是为不用每次去数据库coment表查询',
  `score` double DEFAULT NULL COMMENT '帖子的分数,排名用的',
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `login_ticket`
--


# 登录凭证表,记录用户的登录信息
DROP TABLE IF EXISTS `login_ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `login_ticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `ticket` varchar(45) NOT NULL COMMENT '凭证 是否登录的标识',
  `status` int(11) DEFAULT '0' COMMENT '0-有效; 1-无效;',
  `expired` timestamp NOT NULL COMMENT '凭证的过期时间' ,
  PRIMARY KEY (`id`),
  KEY `index_ticket` (`ticket`(20))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `message`
--


# 私信表
DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_id` int(11) DEFAULT NULL COMMENT '消息的发送人ID',
  `to_id` int(11) DEFAULT NULL COMMENT '消息的接收者ID',
  `conversation_id` varchar(45) NOT NULL COMMENT '会话ID==>标识一对用户对话 有利于查询',
  `content` text,
  `status` int(11) DEFAULT NULL COMMENT '0-未读;1-已读;2-删除;',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_from_id` (`from_id`),
  KEY `index_to_id` (`to_id`),
  KEY `index_conversation_id` (`conversation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS user;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE user (
  id  int(11) NOT NULL AUTO_INCREMENT,
  username varchar(50) DEFAULT NULL,
  password varchar(50) DEFAULT NULL,
  salt varchar(50) DEFAULT NULL,
  email varchar(100) DEFAULT NULL,
  type int(11) DEFAULT NULL COMMENT '0-普通用户; 1-超级管理员; 2-版主;',
  status int(11) DEFAULT NULL COMMENT '0-未激活; 1-已激活;',
  activation_code varchar(100) DEFAULT NULL COMMENT '激活码',
  header_url varchar(200) DEFAULT NULL,
  create_time timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_username` (`username`(20)),
  KEY `index_email` (`email`(20))
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;