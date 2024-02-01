-- ----------------------------
-- 1、文件表
-- ----------------------------
drop table if exists sys_oss;
create table sys_oss (
                          oss_id           bigint(20)      not null auto_increment    comment '文件id',
                          file_name         varchar(100)     default ''                 comment '文件名',
                          original_name         varchar(100)     default ''                 comment '原名',
                          file_suffix         varchar(100)     default ''                 comment '文件后缀名',
                          url         varchar(300)     default ''                 comment 'URL地址',
                          object_name         varchar(300)     default ''                 comment '对象名',
                          bucket         varchar(100)     default ''                 comment '桶名',
                          order_num         int(4)          default 0                  comment '显示顺序',
                          status            char(1)         default '0'                comment '状态（0正常 1停用）',
                          del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
                          create_by         varchar(64)     default ''                 comment '创建者',
                          create_time 	    datetime                                   comment '创建时间',
                          update_by         varchar(64)     default ''                 comment '更新者',
                          update_time       datetime                                   comment '更新时间',
                          primary key (oss_id)
) engine=innodb auto_increment=0 comment = '文件表';
