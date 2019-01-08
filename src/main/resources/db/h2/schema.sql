DROP TABLE IF EXISTS user_info;

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
    gmt_modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
create unique index uk_email on user_info(email);
create unique index uk_username on user_info(username);
