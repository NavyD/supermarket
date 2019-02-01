insert into user_info(username, hash_password, icon_path, email, phone_number, role_id) values('测试用户', '81dc9bdb52d04dc20036dbd8313ed055', '/test/user/1', 'aabb@cc.dd', '13344445555', '1');

insert into role_info(role_name) values('用户'), ('管理员'), ('超级管理员'), ('中级用户'), ('高级用户'), ('仓库管理员'), ('采购管理员'), ('总经理');

insert into user_role(user_id, role_id) values(1, 1), (1, 2), (1, 3)