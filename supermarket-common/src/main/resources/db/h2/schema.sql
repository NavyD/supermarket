drop table if exists user_info;
create table user_info (
	id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    hash_password CHAR(70) NOT NULL,
    icon_path VARCHAR(255) ,
    email varchar(100) not null,
    is_enabled tinyint unsigned not null default 1,
    phone_number char(15),
    failed_count tinyint unsigned not null default 0,
    gmt_create DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
create unique index uk_email on user_info(email);
create unique index uk_username on user_info(username);


drop table if exists role_info;
create table role_info (
	id int unsigned auto_increment primary key, 
    role_name varchar(20) not null,
    is_enabled tinyint unsigned not null default 1,
	gmt_create DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
create unique index uk_rolename on role_info(role_name);

drop table if exists user_role;
create table user_role (
    id int unsigned auto_increment primary key, 
    user_id int unsigned not null,
    role_id int unsigned not null,
	gmt_create DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
create unique index uk_userid_roleid on user_role(user_id, role_id);

drop table if exists product;
create table product(
    id int unsigned auto_increment primary key,
    product_name varchar(100) not null,
    -- 产品图标
    icon_path varchar(255) not null,
    -- 图片路径
    images_path varchar(255) default null,
    -- 生产日期
    production_date date not null,
    -- 保质期
    shelf_life smallint unsigned not null default 65535,
    -- 单位 如一件，一对
    product_unit smallint unsigned not null,
    -- 重量
    weight float unsigned not null,
    -- 重量单位
    weight_unit smallint not null,
    -- 关联分类id
    product_category_id int unsigned not null,
    -- 供应商
    supplier_id int unsigned not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp,
);
create unique index uk_productname on product(product_name);
create index idx_supplierid on product(supplier_id);
create index idx_productcategoryid on product(product_category_id);

