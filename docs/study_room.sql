create database `study_room`;

-- 用户

CREATE TABLE `user` (
    `id`         INT(10)   NOT NULL AUTO_INCREMENT,
    `username`   VARCHAR(64) UNIQUE DEFAULT '' COMMENT '用户名',
    `password`   VARCHAR(64)        DEFAULT '' COMMENT '密码',
    `authority`  TINYINT(1) UNSIGNED   DEFAULT 0 COMMENT '是否是管理员权限 : 1 -> 是，0 -> 否',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = `utf8mb4` COMMENT ='管理员和用户表';

INSERT INTO `user` (`username`,`password`,`authority`) VALUES ('admin','123456',1);

-- 座位表

CREATE TABLE `seat` (
    `id`                 INT(10)          NOT NULL AUTO_INCREMENT,
    `about`  TINYINT(1) UNSIGNED   DEFAULT 0 COMMENT '是否是预约状态 : 1 -> 是，0 -> 否',
    `repair`  TINYINT(1) UNSIGNED   DEFAULT 0 COMMENT '是否是维修状态 : 1 -> 是，0 -> 否',
--     `state`  TINYINT(1) UNSIGNED   DEFAULT 0 COMMENT '座位状态 : 0 -> 未预约，1 -> 已预约，2 -> 维修中',
    `created_at`         TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`         TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = `utf8mb4` COMMENT ='座位表';

-- 订单表

CREATE TABLE `order` (
    `id`          INT(10)          NOT NULL AUTO_INCREMENT,
    `seat_id`            INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联座位ID',
    `user_id`            INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联用户ID',
    `state`  TINYINT(1) UNSIGNED   DEFAULT 1 COMMENT '订单状态 : 1 -> 预约中，0 -> 已取消',
    `created_at`  TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = `utf8mb4` COMMENT ='订单表';


