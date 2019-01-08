create table user_info (
	id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    hash_password CHAR(50) NOT NULL,
    icon_path VARCHAR(255) ,
    email varchar(100) not null,
    is_enabled tinyint unsigned not null default 1,
    phone_number char(15),
    role_id int unsigned not null,
    gmt_create DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    unique key uk_email(email(100)),
    UNIQUE KEY uk_username (username (20))
);

create table role(
	id int unsigned auto_increment primary key, 
    role_name varchar(20) not null,
    is_enabled tinyint unsigned not null default 1,
    parent_id int unsigned not null default 0,
	gmt_create DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    unique index uk_rolename(role_name(20))
);
