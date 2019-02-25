insert into user_info(username, hash_password, email, phone_number) values
('测试用户', '{bcrypt}$2a$10$OFszpBSOVJflK1gwrvKQCOgE7w/zn83amQak1.oRcFIY0DI28i6yq', 'aabb@cc.dd', '13344445555'),
('测试用户2', '{bcrypt}$2a$10$OFszpBSOVJflK1gwrvKQCOgE7w/zn83amQak1.oRcFIY0DI28i6yq', 'ccbb@cc.dd', '12288889999'),
('测试用户3', '{bcrypt}$2a$10$OFszpBSOVJflK1gwrvKQCOgE7w/zn83amQak1.oRcFIY0DI28i6yq', 'eeqq@cc.dd', '12288889922');

insert into role_info(role_name) values('用户'), ('管理员'), ('超级管理员'), ('中级用户'), ('高级用户'), ('仓库管理员'), ('采购管理员'), ('总经理');

insert into user_role(user_id, role_id) values(1, 1), (1, 2), (1, 3);


insert into product(product_name, production_date, shelf_life, product_unit, specification, specification_unit, product_category_id, product_category_name, supplier_id, supplier_name) values
('小龙虾', '2018-07-06', 100, 1, 500, 2, 1, '食品', 1, '供应商1'),
('酸菜鱼','2018-07-12', 50, 1, 88.88, 2, 1, '食品', 1, '供应商1'),
('可口可乐','2018-06-01', 190, 1, 240, 2, 1, '食品', 1, '供应商1');

insert into product_category(id, category_name, parent_id) values 
(1,'食品',0),
(2,'非食品',0),
(3,'生鲜',0),
(4,'饼干',1),
(5,'甜饼',4),
(6,'饮品',1),
(7,'水',6),
(8,'矿泉水',7),
(9,'纯净水',7),
(10,'茶饮料',6),
(11,'红茶',10),
(12,'曲奇',5),
(13,'威化',5),
(14,'酒',1),
(15,'啤酒',14),
(16,'休闲小吃',1);

insert into supplier(supplier_name) values('供应商1'), ('供应商2'), ('供应商3'), ('供应商4'), ('供应商5');