drop database if exists ordersystem;

create database ordersystem;

use ordersystem;

drop table if exists t_user;
create table t_user(
                       `id` int(20) auto_increment not null COMMENT '主键',
                       `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci not null,
                       `gender` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci not null,
                       `department` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci not null,
                       `phonenumber` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci not null,
                       `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci not null unique,
                       `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci not null,
                       `role` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci default 2 not null COMMENT '1是管理员，2是普通用户',
                    PRIMARY KEY (`id`) USING BTREE
)ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

drop table if exists t_food;
create table t_food(
                       `id` int(20) auto_increment not null COMMENT '主键',
                       `foodname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci not null comment '商品名称',
                       `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci not null comment '商品类型',
                       `price` decimal(10) not null comment '商品价格',
                       PRIMARY KEY (`id`) USING BTREE
)ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

drop table if exists t_order_details;
create table t_order_details(
                       `id` int(20) auto_increment not null COMMENT '主键',
                       `amount` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci not null comment '商品数量',
                       `tip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci,
                       `food_id` int(20) null DEFAULT NULL COMMENT '商品id',
                       PRIMARY KEY (`id`) USING BTREE
)ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

insert into t_user(`id`,`name`,`gender`,`department`,`phonenumber`,`username`,`password`,`role`) values('0','admin','男','生产线','13580326893','admin','admin','1');
#alter table  ordersystem.t_user  modify id int auto_increment primary key;
select * from t_user;