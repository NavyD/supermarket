insert into user_info(username, hash_password, icon_path, email, phone_number) values('测试用户', '{bcrypt}$2a$10$OFszpBSOVJflK1gwrvKQCOgE7w/zn83amQak1.oRcFIY0DI28i6yq', '/test/user/1', 'aabb@cc.dd', '13344445555'),('测试用户2', '{bcrypt}$2a$10$OFszpBSOVJflK1gwrvKQCOgE7w/zn83amQak1.oRcFIY0DI28i6yq', '/test/user/2', 'ccbb@cc.dd', '12288889999'),('测试用户3', '{bcrypt}$2a$10$OFszpBSOVJflK1gwrvKQCOgE7w/zn83amQak1.oRcFIY0DI28i6yq', '/test/user/3', 'eeqq@cc.dd', '12288889922');

insert into role_info(role_name) values('用户'), ('管理员'), ('超级管理员'), ('中级用户'), ('高级用户'), ('仓库管理员'), ('采购管理员'), ('总经理');

insert into user_role(user_id, role_id) values(1, 1), (1, 2), (1, 3);


insert into product(product_name, production_date, shelf_life, product_unit, weight, weight_unit, product_category_id, supplier_id) values
('小龙虾', '2018-07-06', 100, 1, 500, 2, 1, 1),
('酸菜鱼','2018-07-12', 50, 1, 88.88, 2, 1, 1),
('可口可乐','2018-06-01', 190, 1, 240, 2, 1, 1);