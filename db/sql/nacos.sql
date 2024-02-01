/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:3306
 Source Schema         : nacos

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 19/01/2024 14:34:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin,
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (11, 'ecerp-dev.yaml', 'DEFAULT_GROUP', 'zhijian:\n  # 名称\n  name: qihangecerp\n  # 版本\n  version: 0.1.0\n  # 版权年份\n  copyrightYear: 2024\n  # 实例演示开关\n  demoEnabled: true\n  # 获取ip地址开关\n  addressEnabled: false\n  # 验证码类型 math 数字计算 char 字符验证\n  captchaType: math\n\n# 防止XSS攻击\nxss:\n  # 过滤开关\n  enabled: true\n  # 排除链接（多个用逗号分隔）\n  excludes: /system/notice\n  # 匹配链接\n  urlPatterns: /system/*,/monitor/*,/tool/*\n\n# mybatis 日志\nmybatis-plus:\n    configuration:\n      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 开启sql日志\n\n\nspring:\n  # redis 配置\n  redis:\n    # 地址\n#    host: 8.130.98.215\n    host: 127.0.0.1\n    # 端口，默认为6379\n    port: 6379\n    # 数据库索引\n    database: 0\n    # 密码\n#    password: 123321\n    # 连接超时时间\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池中的最小空闲连接\n        min-idle: 0\n        # 连接池中的最大空闲连接\n        max-idle: 8\n        # 连接池的最大数据库连接数\n        max-active: 8\n        # #连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driverClassName: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://127.0.0.1:3306/qihang-erp?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n    username: root\n    password: Andy_123\n  \n# minio配置\nminio:\n  endpoint: http://127.0.0.1:9000 #换成自己的minio服务端地址\n  accessKey: 0AksAWDtI20Lpb4qxq5S\n  secretKey: yOAZkOVwrhPooBRel5PB0GwS5uw8VAgmqYykHl3T\n  secure: false\n  bucketName: ecerp', '9ab41e9e6329a955e8947d9b3f9d1dcd', '2024-01-19 13:54:08', '2024-01-19 14:26:38', 'nacos', '127.0.0.1', '', '', '', '', '', 'yaml', '', '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '增加租户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info_beta' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info_tag' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint(0) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(0) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_tag_relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint unsigned NOT NULL COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int unsigned NOT NULL COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL COMMENT '使用量',
  `max_size` int unsigned NOT NULL COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int unsigned NOT NULL COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint unsigned NOT NULL,
  `nid` bigint unsigned NOT NULL,
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin,
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '多租户改造' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (0, 1, 'ecerp-api-dev', 'DEFAULT_GROUP', '', 'zhijian:\r\n  # 名称\r\n  name: qihangerp', 'd8383d16bcbd1ae5eb8a548a6d93f210', '2024-01-19 10:08:39', '2024-01-19 10:08:40', NULL, '127.0.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (1, 2, 'ecerp-api-dev', 'DEFAULT_GROUP', '', 'zhijian:\r\n  # 名称\r\n  name: qihangerp', 'd8383d16bcbd1ae5eb8a548a6d93f210', '2024-01-19 10:23:06', '2024-01-19 10:23:06', 'nacos', '127.0.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (0, 3, 'ecerp-api-druid', 'DEFAULT_GROUP', '', 'zhijian:\r\n    name: qiahng', '73f80892bde4ced9bde010952deed2c2', '2024-01-19 11:49:28', '2024-01-19 11:49:29', NULL, '127.0.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (3, 4, 'ecerp-api-druid', 'DEFAULT_GROUP', '', 'zhijian:\r\n    name: qiahng', '73f80892bde4ced9bde010952deed2c2', '2024-01-19 11:49:50', '2024-01-19 11:49:50', 'nacos', '127.0.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (3, 5, 'ecerp-api-druid', 'DEFAULT_GROUP', '', 'zhijian:\n  # 名称\n  name: qihangerp\n\n# 防止XSS攻击\nxss:\n  # 过滤开关\n  enabled: true\n  # 排除链接（多个用逗号分隔）\n  excludes: /system/notice\n  # 匹配链接\n  urlPatterns: /system/*,/monitor/*,/tool/*', 'c6153c27aa45a37bc88ac33b8328843f', '2024-01-19 13:22:16', '2024-01-19 13:22:16', 'nacos', '127.0.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (3, 6, 'ecerp-api-druid', 'DEFAULT_GROUP', '', 'zhijian:\n  name: qihangerp\n\n# 防止XSS攻击\nxss:\n  # 过滤开关\n  enabled: true\n  # 排除链接（多个用逗号分隔）\n  excludes: /system/notice\n  # 匹配链接\n  urlPatterns: /system/*,/monitor/*,/tool/*', '3fd25dcb5fe20e3095a3d79c0b8bb698', '2024-01-19 13:22:23', '2024-01-19 13:22:23', 'nacos', '127.0.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (0, 7, 'ecerp-druid', 'DEFAULT_GROUP', '', 'zhijian:\r\n  name: qihangerp\r\n\r\n# 防止XSS攻击\r\nxss:\r\n  # 过滤开关\r\n  enabled: true\r\n  # 排除链接（多个用逗号分隔）\r\n  excludes: /system/notice\r\n  # 匹配链接\r\n  urlPatterns: /system/*,/monitor/*,/tool/*', '1142df26f0538a996beface0ea8e530b', '2024-01-19 13:28:47', '2024-01-19 13:28:47', NULL, '127.0.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (1, 8, 'ecerp-api-dev', 'DEFAULT_GROUP', '', 'zhijian:\n  # 名称\n  name: qihangerp\n\n# 防止XSS攻击\nxss:\n  # 过滤开关\n  enabled: true\n  # 排除链接（多个用逗号分隔）\n  excludes: /system/notice\n  # 匹配链接\n  urlPatterns: /system/*,/monitor/*,/tool/*', 'c6153c27aa45a37bc88ac33b8328843f', '2024-01-19 13:31:47', '2024-01-19 13:31:47', NULL, '127.0.0.1', 'D', '', '');
INSERT INTO `his_config_info` VALUES (3, 9, 'ecerp-api-druid', 'DEFAULT_GROUP', '', 'zhijian:\n  name: qihangerp\n\n# 防止XSS攻击\nxss:\n  # 过滤开关\n  enabled: true\n  # 排除链接（多个用逗号分隔）\n  excludes: /system/notice\n  # 匹配链接\n  urlPatterns: /system/*,/monitor/*,/tool/*', '3fd25dcb5fe20e3095a3d79c0b8bb698', '2024-01-19 13:31:56', '2024-01-19 13:31:56', NULL, '127.0.0.1', 'D', '', '');
INSERT INTO `his_config_info` VALUES (0, 10, 'ecerp-druid.yaml', 'DEFAULT_GROUP', '', 'zhijian:\r\n name: qihang', '10075ca081dadff46ace86b6c17755dc', '2024-01-19 13:41:04', '2024-01-19 13:41:04', NULL, '127.0.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (8, 11, 'ecerp-druid.yaml', 'DEFAULT_GROUP', '', 'zhijian:\r\n name: qihang', '10075ca081dadff46ace86b6c17755dc', '2024-01-19 13:46:33', '2024-01-19 13:46:33', 'nacos', '127.0.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (7, 12, 'ecerp-druid', 'DEFAULT_GROUP', '', 'zhijian:\r\n  name: qihangerp\r\n\r\n# 防止XSS攻击\r\nxss:\r\n  # 过滤开关\r\n  enabled: true\r\n  # 排除链接（多个用逗号分隔）\r\n  excludes: /system/notice\r\n  # 匹配链接\r\n  urlPatterns: /system/*,/monitor/*,/tool/*', '1142df26f0538a996beface0ea8e530b', '2024-01-19 13:47:18', '2024-01-19 13:47:19', NULL, '127.0.0.1', 'D', '', '');
INSERT INTO `his_config_info` VALUES (0, 13, 'ecerp-druid.yml', 'DEFAULT_GROUP', '', 'zhijian:\r\n    name: qihangerp', '308c7b9f25c49f170aea67f61b10dc72', '2024-01-19 13:47:59', '2024-01-19 13:48:00', NULL, '127.0.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 14, 'ecerp-dev.yaml', 'DEFAULT_GROUP', '', 'zhijian:\r\n  name: qihangerp\r\n\r\n# 防止XSS攻击\r\nxss:\r\n  # 过滤开关\r\n  enabled: true\r\n  # 排除链接（多个用逗号分隔）\r\n  excludes: /system/notice\r\n  # 匹配链接\r\n  urlPatterns: /system/*,/monitor/*,/tool/*', '1142df26f0538a996beface0ea8e530b', '2024-01-19 13:54:07', '2024-01-19 13:54:08', NULL, '127.0.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (11, 15, 'ecerp-dev.yaml', 'DEFAULT_GROUP', '', 'zhijian:\r\n  name: qihangerp\r\n\r\n# 防止XSS攻击\r\nxss:\r\n  # 过滤开关\r\n  enabled: true\r\n  # 排除链接（多个用逗号分隔）\r\n  excludes: /system/notice\r\n  # 匹配链接\r\n  urlPatterns: /system/*,/monitor/*,/tool/*', '1142df26f0538a996beface0ea8e530b', '2024-01-19 14:04:26', '2024-01-19 14:04:27', 'nacos', '127.0.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (11, 16, 'ecerp-dev.yaml', 'DEFAULT_GROUP', '', 'zhijian:\n  name: qihangerp\n\n# 防止XSS攻击\nxss:\n  # 过滤开关\n  enabled: true\n  # 排除链接（多个用逗号分隔）\n  excludes: /system/notice\n  # 匹配链接\n  urlPatterns: /system/*,/monitor/*,/tool/*\n\nspring:\n  # redis 配置\n  redis:\n    # 地址\n#    host: 8.130.98.215\n    host: 127.0.0.1\n    # 端口，默认为6379\n    port: 6379\n    # 数据库索引\n    database: 0\n    # 密码\n#    password: 123321\n    # 连接超时时间\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池中的最小空闲连接\n        min-idle: 0\n        # 连接池中的最大空闲连接\n        max-idle: 8\n        # 连接池的最大数据库连接数\n        max-active: 8\n        # #连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms', '2deb901938451202115e2895691c5d48', '2024-01-19 14:07:01', '2024-01-19 14:07:02', 'nacos', '127.0.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (10, 17, 'ecerp-druid.yml', 'DEFAULT_GROUP', '', 'zhijian:\r\n    name: qihangerp', '308c7b9f25c49f170aea67f61b10dc72', '2024-01-19 14:15:39', '2024-01-19 14:15:39', NULL, '127.0.0.1', 'D', '', '');
INSERT INTO `his_config_info` VALUES (8, 18, 'ecerp-druid.yaml', 'DEFAULT_GROUP', '', 'zhijian:\n  name: qihangerp\n\n# 防止XSS攻击\nxss:\n  # 过滤开关\n  enabled: true\n  # 排除链接（多个用逗号分隔）\n  excludes: /system/notice\n  # 匹配链接\n  urlPatterns: /system/*,/monitor/*,/tool/*', '3fd25dcb5fe20e3095a3d79c0b8bb698', '2024-01-19 14:15:44', '2024-01-19 14:15:44', NULL, '127.0.0.1', 'D', '', '');
INSERT INTO `his_config_info` VALUES (11, 19, 'ecerp-dev.yaml', 'DEFAULT_GROUP', '', 'zhijian:\n  name: qihangerp\n\n# 防止XSS攻击\nxss:\n  # 过滤开关\n  enabled: true\n  # 排除链接（多个用逗号分隔）\n  excludes: /system/notice\n  # 匹配链接\n  urlPatterns: /system/*,/monitor/*,/tool/*\nmybatis-plus:\n    configuration:\n      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 开启sql日志\nspring:\n  # redis 配置\n  redis:\n    # 地址\n#    host: 8.130.98.215\n    host: 127.0.0.1\n    # 端口，默认为6379\n    port: 6379\n    # 数据库索引\n    database: 0\n    # 密码\n#    password: 123321\n    # 连接超时时间\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池中的最小空闲连接\n        min-idle: 0\n        # 连接池中的最大空闲连接\n        max-idle: 8\n        # 连接池的最大数据库连接数\n        max-active: 8\n        # #连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms', '9a64eaad9c2053f93742be05c557f475', '2024-01-19 14:16:38', '2024-01-19 14:16:38', 'nacos', '127.0.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (11, 20, 'ecerp-dev.yaml', 'DEFAULT_GROUP', '', 'zhijian:\n  # 名称\n  name: qihangecerp\n  # 版本\n  version: 0.1.0\n  # 版权年份\n  copyrightYear: 2024\n  # 实例演示开关\n  demoEnabled: true\n  # 获取ip地址开关\n  addressEnabled: false\n  # 验证码类型 math 数字计算 char 字符验证\n  captchaType: math\n\n# 防止XSS攻击\nxss:\n  # 过滤开关\n  enabled: true\n  # 排除链接（多个用逗号分隔）\n  excludes: /system/notice\n  # 匹配链接\n  urlPatterns: /system/*,/monitor/*,/tool/*\n\n# mybatis 日志\nmybatis-plus:\n    configuration:\n      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 开启sql日志\n\n\nspring:\n  # redis 配置\n  redis:\n    # 地址\n#    host: 8.130.98.215\n    host: 127.0.0.1\n    # 端口，默认为6379\n    port: 6379\n    # 数据库索引\n    database: 0\n    # 密码\n#    password: 123321\n    # 连接超时时间\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池中的最小空闲连接\n        min-idle: 0\n        # 连接池中的最大空闲连接\n        max-idle: 8\n        # 连接池的最大数据库连接数\n        max-active: 8\n        # #连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms', '666f732d9ab6078036d2fe1a9158cd0c', '2024-01-19 14:19:38', '2024-01-19 14:19:39', 'nacos', '127.0.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (11, 21, 'ecerp-dev.yaml', 'DEFAULT_GROUP', '', 'zhijian:\n  # 名称\n  name: qihangecerp\n  # 版本\n  version: 0.1.0\n  # 版权年份\n  copyrightYear: 2024\n  # 实例演示开关\n  demoEnabled: true\n  # 获取ip地址开关\n  addressEnabled: false\n  # 验证码类型 math 数字计算 char 字符验证\n  captchaType: math\n\n# 防止XSS攻击\nxss:\n  # 过滤开关\n  enabled: true\n  # 排除链接（多个用逗号分隔）\n  excludes: /system/notice\n  # 匹配链接\n  urlPatterns: /system/*,/monitor/*,/tool/*\n\n# mybatis 日志\nmybatis-plus:\n    configuration:\n      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 开启sql日志\n\n\nspring:\n  # redis 配置\n  redis:\n    # 地址\n#    host: 8.130.98.215\n    host: 127.0.0.1\n    # 端口，默认为6379\n    port: 6379\n    # 数据库索引\n    database: 0\n    # 密码\n#    password: 123321\n    # 连接超时时间\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池中的最小空闲连接\n        min-idle: 0\n        # 连接池中的最大空闲连接\n        max-idle: 8\n        # 连接池的最大数据库连接数\n        max-active: 8\n        # #连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n# minio\nminio:\n  endpoint: http://127.0.0.1:9000 #换成自己的minio服务端地址\n  accessKey: 0AksAWDtI20Lpb4qxq5S\n  secretKey: yOAZkOVwrhPooBRel5PB0GwS5uw8VAgmqYykHl3T\n  secure: false\n  bucketName: ecerp', 'd81909d80ccc73dffdabf64fa6a7e862', '2024-01-19 14:20:44', '2024-01-19 14:20:44', 'nacos', '127.0.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (11, 22, 'ecerp-dev.yaml', 'DEFAULT_GROUP', '', 'zhijian:\n  # 名称\n  name: qihangecerp\n  # 版本\n  version: 0.1.0\n  # 版权年份\n  copyrightYear: 2024\n  # 实例演示开关\n  demoEnabled: true\n  # 获取ip地址开关\n  addressEnabled: false\n  # 验证码类型 math 数字计算 char 字符验证\n  captchaType: math\n\n# 防止XSS攻击\nxss:\n  # 过滤开关\n  enabled: true\n  # 排除链接（多个用逗号分隔）\n  excludes: /system/notice\n  # 匹配链接\n  urlPatterns: /system/*,/monitor/*,/tool/*\n\n# mybatis 日志\nmybatis-plus:\n    configuration:\n      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 开启sql日志\n\n\nspring:\n  # redis 配置\n  redis:\n    # 地址\n#    host: 8.130.98.215\n    host: 127.0.0.1\n    # 端口，默认为6379\n    port: 6379\n    # 数据库索引\n    database: 0\n    # 密码\n#    password: 123321\n    # 连接超时时间\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池中的最小空闲连接\n        min-idle: 0\n        # 连接池中的最大空闲连接\n        max-idle: 8\n        # 连接池的最大数据库连接数\n        max-active: 8\n        # #连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n# minio\n# minio:\n#   endpoint: http://127.0.0.1:9000 #换成自己的minio服务端地址\n#   accessKey: 0AksAWDtI20Lpb4qxq5S\n#   secretKey: yOAZkOVwrhPooBRel5PB0GwS5uw8VAgmqYykHl3T\n#   secure: false\n#   bucketName: ecerp', 'ba7138fe96e9993498c622d2f9e18c67', '2024-01-19 14:21:34', '2024-01-19 14:21:34', 'nacos', '127.0.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (11, 23, 'ecerp-dev.yaml', 'DEFAULT_GROUP', '', 'zhijian:\n  # 名称\n  name: qihangecerp\n  # 版本\n  version: 0.1.0\n  # 版权年份\n  copyrightYear: 2024\n  # 实例演示开关\n  demoEnabled: true\n  # 获取ip地址开关\n  addressEnabled: false\n  # 验证码类型 math 数字计算 char 字符验证\n  captchaType: math\n\n# 防止XSS攻击\nxss:\n  # 过滤开关\n  enabled: true\n  # 排除链接（多个用逗号分隔）\n  excludes: /system/notice\n  # 匹配链接\n  urlPatterns: /system/*,/monitor/*,/tool/*\n\n# mybatis 日志\nmybatis-plus:\n    configuration:\n      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 开启sql日志\n\n\nspring:\n  # redis 配置\n  redis:\n    # 地址\n#    host: 8.130.98.215\n    host: 127.0.0.1\n    # 端口，默认为6379\n    port: 6379\n    # 数据库索引\n    database: 0\n    # 密码\n#    password: 123321\n    # 连接超时时间\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池中的最小空闲连接\n        min-idle: 0\n        # 连接池中的最大空闲连接\n        max-idle: 8\n        # 连接池的最大数据库连接数\n        max-active: 8\n        # #连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n# minio配置\nminio:\n  endpoint: http://127.0.0.1:9000 #换成自己的minio服务端地址\n  accessKey: 0AksAWDtI20Lpb4qxq5S\n  secretKey: yOAZkOVwrhPooBRel5PB0GwS5uw8VAgmqYykHl3T\n  secure: false\n  bucketName: ecerp', 'fc534ac4be7aa2bc7832d899192ab56a', '2024-01-19 14:24:49', '2024-01-19 14:24:50', 'nacos', '127.0.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (11, 24, 'ecerp-dev.yaml', 'DEFAULT_GROUP', '', 'zhijian:\n  # 名称\n  name: qihangecerp\n  # 版本\n  version: 0.1.0\n  # 版权年份\n  copyrightYear: 2024\n  # 实例演示开关\n  demoEnabled: true\n  # 获取ip地址开关\n  addressEnabled: false\n  # 验证码类型 math 数字计算 char 字符验证\n  captchaType: math\n\n# 防止XSS攻击\nxss:\n  # 过滤开关\n  enabled: true\n  # 排除链接（多个用逗号分隔）\n  excludes: /system/notice\n  # 匹配链接\n  urlPatterns: /system/*,/monitor/*,/tool/*\n\n# mybatis 日志\nmybatis-plus:\n    configuration:\n      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 开启sql日志\n\n\nspring:\n  # redis 配置\n  redis:\n    # 地址\n#    host: 8.130.98.215\n    host: 127.0.0.1\n    # 端口，默认为6379\n    port: 6379\n    # 数据库索引\n    database: 0\n    # 密码\n#    password: 123321\n    # 连接超时时间\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池中的最小空闲连接\n        min-idle: 0\n        # 连接池中的最大空闲连接\n        max-idle: 8\n        # 连接池的最大数据库连接数\n        max-active: 8\n        # #连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driverClassName: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://127.0.0.1:3306/qihang-erp?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n    username: root\n    password: Andy_123\n  \n# minio配置\nminio:\n  endpoint: http://127.0.0.1:9000 #换成自己的minio服务端地址\n  accessKey: 0AksAWDtI20Lpb4qxq5S\n  secretKey: yOAZkOVwrhPooBRel5PB0GwS5uw8VAgmqYykHl3T\n  secure: false\n  bucketName: ecerp', '9ab41e9e6329a955e8947d9b3f9d1dcd', '2024-01-19 14:25:51', '2024-01-19 14:25:51', 'nacos', '127.0.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (11, 25, 'ecerp-dev.yaml', 'DEFAULT_GROUP', '', 'zhijian:\n  # 名称\n  name: qihangecerp\n  # 版本\n  version: 0.1.0\n  # 版权年份\n  copyrightYear: 2024\n  # 实例演示开关\n  demoEnabled: true\n  # 获取ip地址开关\n  addressEnabled: false\n  # 验证码类型 math 数字计算 char 字符验证\n  captchaType: math\n\n# 防止XSS攻击\nxss:\n  # 过滤开关\n  enabled: true\n  # 排除链接（多个用逗号分隔）\n  excludes: /system/notice\n  # 匹配链接\n  urlPatterns: /system/*,/monitor/*,/tool/*\n\n# mybatis 日志\nmybatis-plus:\n    configuration:\n      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 开启sql日志\n\n\nspring:\n  # redis 配置\n  redis:\n    # 地址\n#    host: 8.130.98.215\n    host: 127.0.0.1\n    # 端口，默认为6379\n    port: 6379\n    # 数据库索引\n    database: 0\n    # 密码\n#    password: 123321\n    # 连接超时时间\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池中的最小空闲连接\n        min-idle: 0\n        # 连接池中的最大空闲连接\n        max-idle: 8\n        # 连接池的最大数据库连接数\n        max-active: 8\n        # #连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driverClassName: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://127.0.0.1:3306/qihang-erp1?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n    username: root\n    password: Andy_123\n  \n# minio配置\nminio:\n  endpoint: http://127.0.0.1:9000 #换成自己的minio服务端地址\n  accessKey: 0AksAWDtI20Lpb4qxq5S\n  secretKey: yOAZkOVwrhPooBRel5PB0GwS5uw8VAgmqYykHl3T\n  secure: false\n  bucketName: ecerp', '64c56378226ed99da24f05cf083fa525', '2024-01-19 14:26:38', '2024-01-19 14:26:38', 'nacos', '127.0.0.1', 'U', '', '');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username`, `role`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint unsigned NOT NULL COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int unsigned NOT NULL COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL COMMENT '使用量',
  `max_size` int unsigned NOT NULL COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL COMMENT '聚合子配置最大个数',
  `max_aggr_size` int unsigned NOT NULL COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '租户容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'tenant_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
