# Design

## User Module

### password

使用spring security提供的接口`PasswordEncoder`及代理实现`DelegatingPasswordEncoder`与hash算法`bcrypt`。

`DelegatingPasswordEncoder`具有特定的格式用于方便加密与匹配。格式为`{id}hashedpassword`.

如：`{bcrypt}$2a$10$OFszpBSOVJflK1gwrvKQCOgE7w/zn83amQak1.oRcFIY0DI28i6yq`

即设计字段`hash_password`长度为70

### register

注册用户，通过使用安全码的形式注册，必须保证安全码正确才能注册成功。

当前仅支持邮箱验证码发送

当用户注册时，用户填写邮箱后点击发送验证码，登录邮箱获取验证码后将其输入到注册表单中，然后提交，如果验证码正确并其他信息无错误则注册成功。

### forgot password

忘记密码，通过安全码的形式重置密码。也允许通过旧密码重置密码

### secure code

所谓的安全码即简单的实现为固定长度的随机字符串。

用户地址作为key，code作为value的缓存，code被使用时允许主动移除，也允许自动过期。

### Login

登录策略

#### 安全

##### 防止暴力登录

使用failedCount字段防止暴力破解登录系统，如果用户登录失败，则failedCount++，如果超过一定次数，则**锁定用户**。

当用户被锁定后一段时间未登录则自动解除锁定，允许登录。如果用户在首次解除锁定后再次登录失败则增加锁定时间。

###### 策略

默认锁定时间为30秒， 登录失败5次后开始锁定。

如果用户首次锁定后仍然登录失败，简单的将时间加倍`(times + 1) * baseLimitSecond`

### 数据表

注意：所有的表兼容MySQL，h2语法

```sql
drop table if exists user_info;
create table user_info (
    id int unsigned auto_increment primary key,
    username varchar(20) not null,
    -- 使用spring security DelegatingPasswordEncoder格式{id}+bcrypt
    hash_password char(70) not null,
    icon_path varchar(255) ,
    email varchar(100) not null,
    -- 尝试登录失败次数
    failed_count tinyint unsigned not null default 0,
    -- 账户激活
    is_enabled tinyint unsigned not null default 1,
    phone_number char(15) default null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp,
);
-- index兼容写法
create unique index uk_email on user_info(email);
create unique index uk_username on user_info(username);
```

### references

- [The definitive guide to form-based website authentication [closed]](https://stackoverflow.com/questions/549/the-definitive-guide-to-form-based-website-authentication/477578#477578)

## Role Module

基于spring security Access-Control实现。

对于权限管理，使用方法级别的控制，暂时不需要具体的资源权限，控制应该交给web层。可能需要增加权限管理模块，应该需要将资源具体化对应到方法，碰到再说

### 权限管理

超级管理员有任何权限

对于订单部分，存在`user_id`字段，只有该用户、管理员可见，该用户可以操作订单status，但不可修改订单敏感信息，

如果user1下进货单，完成前需要将商品入库，此时可能该用户没有入库权限，需要另一个用户user2入库，入库记录单则会记录入库操作员user2。如果有入库权限则user1也可以入库

### 数据表

```sql
drop table if exists role_info;
create table role_info (
    id int unsigned auto_increment primary key,
    role_name varchar(20) not null,
    is_enabled tinyint unsigned not null default 1,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
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
```

## Category

使用邻接表结构。由于目录结构修改及少，查询较多，可以使用缓存忽略递归查询的消耗

### 查询

- 查询某节点下一级

```sql
select * from product_category where parent_id = #{id}
```

- 查询某节点下所有的节点

```sql
-- 仅适用于mysql（find_in_set函数）
select id, category_name, parent_id, gmt_create, gmt_modified
from (select id, category_name, parent_id, gmt_create, gmt_modified 
    from product_category
    order by parent_id, id) sorted_category,
    (select @pid := '1') initialisation
where find_in_set(parent_id, @pid) and length(@pid := concat(@pid, ',', id))

-- cte 适用于mysql 8, h2
with recursive cte (id, category_name, parent_id, gmt_create, gmt_modified) as (
    select id, category_name, parent_id, gmt_create, gmt_modified
    from product_category
    where parent_id = 1
    union all
    select p.id, p.category_name, p.parent_id, p.gmt_create, p.gmt_modified
    from product_category p inner join cte on p.parent_id = cte.id
)
select * from cte;
```

### 数据表

```sql
drop table if exists product_category;
create table product_category(
    id int unsigned auto_increment primary key,
    category_name varchar(20) not null,
    parent_id int unsigned not null default 0,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
);
create unique index uk_categoryname on product_category(category_name);

insert into product_category(id, category_name, parent_id) values (1,'食品',0),(2,'非食品',0),(3,'生鲜',0),(4,'饼干',1),(5,'甜饼',4),(6,'饮品',1),(7,'水',6),(8,'矿泉水',7),(9,'纯净水',7),(10,'茶饮料',6),(11,'红茶',10),(12,'曲奇',5),(13,'威化',5),(14,'酒',1),(15,'啤酒',14),(16,'休闲小吃',1)
```

### 基于ClosureTable的无限级分类存储

对于如下数据

```
1
| - 2
     | - 3
          | - 6  
     | - 5
| - 4
```

#### insert

```sql
insert into category_closure(ancestor, descendant, distance) values
(0， 1， 0)
(1, 1, 0)
(0, 2, 1)
(1, 2, 1)
()
```

#### 数据表

```sql
drop table if exists product_category;
create table product_category(
    id int unsigned auto_increment primary key,
    category_name varchar(20) not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
);
create unique index uk_categoryname on product_category(category_name);

drop table if exists category_closure;
create table category_closure(
    id int unsigned auto_increment primary key,
    ancestor int unsigned not null,
    descendant int unsigned not null,
    distance int unsigned not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
);
create unique index uk_ancestor_descendant_distance on category_closure(ancestor, descendant, distance);
```

### References

- [在数据库中存储一棵树，实现无限级分类](https://segmentfault.com/a/1190000014284076#articleHeader7)
- [Closure Tables for Browsing Trees in SQL](https://coderwall.com/p/lixing/closure-tables-for-browsing-trees-in-sql)
- [How to create a MySQL hierarchical recursive query](https://stackoverflow.com/a/33737203/8566831)

## Product

商品，仅表示商品本身信息，额外的信息由其他表定义。如数量，价格

### 图片

支持上传图片，允许多张图片，数据库不保存实际的图片地址路径，具体的逻辑交给service

可能存在多种类型图片，如icon，用于展示略缩图，image用于展示具体产品图

可以使用在service中获取部分特定的图片，不需要存储不同的图片类型

具体的地址逻辑可以是：在某个指定路径下存储与product_id的路径如`/images/products/{id}`

### 过期

实现商品过期功能。记录商品生产日期与保质期时间，

### 重量

记录商品重量与单位。可以使用重量实现功能，仓库重量限制，重量计价等

#### 单位

```java
@Getter
public enum ProductWeightUnitEnum {
  MICROGRAM(1), GRAM(2), KILOGRAM(3), TONNE(4);
  
  private final int sequence;
  
  ProductWeightUnitEnum(int sequence) {
    this.sequence = sequence;
  }
}
```

### api

```java
boolean isExpired();
```

### 数据表

```sql
drop table if exists product;
create table product(
    id int unsigned auto_increment primary key,
    product_name varchar(100) not null,
    -- 生产日期
    production_date date not null,
    -- 保质期
    shelf_life smallint unsigned not null default 65535,
    -- 单位 如一件，一对
    product_unit tinyint unsigned not null,
    -- 重量
    weight float unsigned not null,
    -- 重量单位
    weight_unit tinyint unsigned not null,
    -- 关联分类id
    product_category_id int unsigned not null,
    -- 供应商
    supplier_id int unsigned not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
);
create unique index uk_productname on product(product_name);
create index idx_supplierid on product(supplier_id);
create index idx_productcategoryid on product(product_category_id);

insert into product(product_name, production_date, shelf_life, product_unit, weight, weight_unit, product_category_id, supplier_id) values
('小龙虾', '2018-07-06', 100, 1, 500, 2, 1, 1),
('酸菜鱼','2018-07-12', 50, 1, 88.88, 2, 1, 1),
('可口可乐','2018-06-01', 190, 1, 240, 2, 1, 1);
```

### shelf

货架，上架销售的商品

价格

当一个商品需要上架时应该设置商品价格，包括零售、vip、促销。零售价格应该必须设置，vip、促销价格默认为0.00表示未设置

#### 数据表

```sql
drop table if exists shelf;
create table shelf(
    id int unsigned auto_increment primary key,
    shelf_name varchar(50) not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp,
);
create unique index uk_shelfname on shelf(shelf_name);

drop table if exists shelf_item;
create table shelf_item(
    id int unsigned auto_increment primary key,
    -- 上架价格
    retail_price decimal(15, 2) unsigned not null default 0.00,
    promotional_price decimal(15,2) unsigned not null default 0.00,
    vip_price decimal(15,2) unsigned not null default 0.00,
    -- 上架数量
    quantity smallint unsigned not null default 0,
    product_id int unsigned not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp,
);
create index idx_productid on shelf_product(product_id);
```

## repository

仓库，保存商品的仓库

### 重量

仓库不保存重量单位，可以在service中默认指定一个单位，如千克，当保存商品时需要转换单位并检查是否超出

```sql
drop table if exists repository;
create table repository(
    id int unsigned auto_increment primary key,
    repository_name varchar(50) not null,
    -- 单个仓库重量限制
    max_weight int unsigned not null default 4294967295,
    min_weight int unsigned not null default 0,
    weight_unit tinyint unsigned not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp,
);
create unique index repositoryname on repository(repository_name);
```

### repository item

仓库商品重量、数量限制，允许对一种商品管理重量、数量

```sql
drop table if exists repository_item;
create table repository_item(
    id int unsigned auto_increment primary key,
    product_id int unsigned not null,
    repository_id int unsigned not null,
    quantity smallint unsigned not null default 0,
    -- 重量限制
    max_weight int unsigned not null default 4294967295,
    min_weight int unsigned not null default 0,
    weight_unit tinyint unsigned not null,
    -- 数量限制
    max_quantity smallint unsigned not null  default 65535,
    min_quantity smallint unsigned not null default 0,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp,
);
create unique index uk_repositoryid_productid on repository_item(repository_id, product_id);
```

## supplier

```sql
drop table if exists supplier;
create table supplier (
    id int unsigned auto_increment primary key,
    supplier_name varchar(50) not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp,
);
create unique index uk_suppliername on supplier(supplier_name);
```

### supplier record

供应商品。记录商品不同时间的供应单价，可以使用作为分析统计，进货单建议单价，或在进货时设置新的单价时增加到供应价格。记录进货退货单价，在进货退货时选择

#### 供货价格

所有的供货退货价格应该从supplier record的最新记录中获取，如果最新记录没有，则应该添加一个新的价格，即使该价格与之前某个一样，这样方便分析统计

价格分为进货单价与退货单价，不允许为null,即如果供应商仅更新了一个价格，那应该插入一个新的记录，另一个价格为原来的价格。冗余

- 查询指定商品最新进货价格

```sql
-- 1.子查询
select a.*
from supplier_record
inner join (
    select product_id, max(time_supplied) time_supplied
    from supplier_record
    group by product_id) b
on a.product_id = b.product_id and a.time_supplied = b.time_supplied
where a.product_id = 1

-- 2.左连接 性能更好
select a.*
from supplier_record a
left outer join supplier_record b
-- 过滤掉time_supplied>=，去除重复，保证当a.time_supplied(max)连接b时对应null
on a.product_id = b.product_id and a.time_supplied < b.time_supplied 
where b.product_id is null and a.product_id = 1
```

### 数据表

```sql
drop table if exists supplier_record;
create table supplier_record(
    id int unsigned auto_increment primary key,
    -- 供应的时间
    time_supplied datetime not null,
    -- 供应的价格
    unit_price_supply decimal(15, 2) unsigned not null,
    unit_price_return decimal(15, 2) unsigned not null,
    product_id int unsigned not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
);
create unique index uk_productid_timesupplied on supplier_record(product_id, time_supplied);

insert into supplier_record (time_supplied, unit_price_supply, unit_price_return, product_id) values('2018-07-10', 101.11, 98, 1), ('2018-07-12', 100.1, 90.1, 1), ('2018-07-19', 100.1, 90.1, 1),('2018-07-15', 15.2, 10.1, 2);
```

### dao

```java
  T getLastByProductId(Integer productId);
```

### references

[SQL select only rows with max value on a column](https://stackoverflow.com/a/7745635/8566831)

## 订单

在本系统中，所有关于商品的出入都使用订单的形式记录

### 订单类型

所有类型都需要一个订单编号

- 商品入库

进货、下架、商品转库

- 商品出库

上架、进货商品回退、商品转库

如果本系统要扩展为一个后台管理+电商组合系统，后台管理应该提供货物即可，即将库存的货供给到电商系统中。电商系统从后台系统api获取库存并销售

后台不需要销售功能，如果需要销售信息统计，电商系统应该提供某些api交互，形成相互依赖关系（？）

#### 订单状态机

创建订单    ==>     未支付  ==>     已支付  ==>     审核    ==>     入库    ==> 交易完成

```java
public interface PurchaseOrderState {
    // 正常操作
    void pay();
    void takeDelivery();
    void check();
    void putStorage();
    void finish();

    void cancel();
}
```

### 进货订单

订单状态：审查、支付、验收、入库、完成、取消

不可变：已完成、已取消、审查失败、验收失败

取消订单，只能在审查、支付、待验收时执行，如果已支付则退款

如果支付方式为在线支付，状态为审查、支付、验收，否则为审查、验收、支付

```sql
drop table if exists purchase_order;
create table purchase_order(
    id int unsigned auto_increment primary key,
    order_no bigint unsigned not null,
    -- 订单计算总价
    total_price decimal(15 , 2) not null,
    -- 实际付款金额
    payment_amount decimal(15, 2) not null default 0.00,
    -- 已支付
    is_paid tinyint not null default 0,
    -- 支付方式 在线支付 支付宝、微信、货到付款
    payment_type tinyint unsigned not null,
    -- 支付时间
    payment_time datetime default null,
    -- 操作人id
    user_id int unsigned not null,
    -- 订单状态。
    order_status tinyint unsigned not null default 1,
    remark varchar(1000) not null default '',
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
);
create unique index uk_orderno on purchase_order(order_no);

drop table if exists purchase_order_item;
create table purchase_order_item(
    id int unsigned auto_increment primary key,
    product_id int unsigned not null,
    product_name varchar(100) not null,
    unit_price decimal(15, 2) not null,
    quantity smallint unsigned not null,
    -- 表示 小计价格
    total_price decimal(15, 2) not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
)
```

### 进货退货订单

状态：未支付，已支付，已出库，已完成，已取消

退货单价应该从supplier record中获取

```sql
create table purchase_return_order(
    id int unsigned auto_increment primary key,
    order_no bigint unsigned not null,
    -- 订单计算总价
    total_price decimal(15 , 2) not null,
    -- 实际付款金额
    payment_amount decimal(15, 2) not null,
    -- 支付时间
    payment_time datetime default null,
    -- 操作人id
    user_id int unsigned not null,
    -- 存入的仓库
    repository_id int unsigned not null,
    -- 订单状态。未支付，已支付，已发货，交易完成，已取消，已退货
    order_status tinyint unsigned not null default 1,
    -- 订单完成时间。即订单状态不可变化时
    finish_time datetime default null,
    remark varchar(1000) not null default '',
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
)；

create table purchase_return_order_item(
    id int unsigned auto_increment primary key,
    product_id int unsigned not null,
    product_name varchar(100) not null,
    -- 退货单价
    unit_price decimal(15, 2) not null,
    quantity smallint unsigned not null,
    -- 表示 小计价格
    total_price decimal(15, 2) not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
)
```

#### 货架订单

该订单表示对货架进行上下架操作，需要结合库存记录表一起完成。如果inventory_record.is_outbound=true则表示为上架订单，否则下架

订单item只有shelf_id而没有repository_id，因为需要使用库存记录表入库出库

```sql
/*

create table shelf_order(
    id int unsigned auto_increment primary key,
    order_no bigint unsigned not null,
    -- 上架
    is_shelved tinyint unsigned not null,
    -- 操作人id
    user_id int unsigned not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
);

*/

create table shelf_order_item(
    id int unsigned auto_increment primary key,
    -- 具体货架 下架则表示从该货架取出，上架则到该货架上
    shelf_id int unsigned not null,
    product_id int unsigned not null,
    quantity smallint unsigned not null,
    -- 库存记录
    inventory_record_id int unsigned not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
)
```

### 转库

转库功能使用库存记录表实现，只需要使用一个额外的订单号

### 库存记录

商品入库记录.所有商品进入库存都需要该记录

支持进货入库，库存移动，上下架，销售退货入库操作，所有类型都需要一个订单编号

所有的order_no应该直接可以通过几个bit确定订单类型，不再需要额外存储订单类型

一个订单的商品，允许进入不同的仓库，同种商品可以用分开不同的数量进入各种仓库

进货订单入库。当进货订单需要完成时，必须将订单所有的商品入库。

```sql
create table inventory_record(
    id int unsigned auto_increment primary key,
    -- 是否是出库订单 否则为入库
    is_outbound tinyint unsigned not null,
    -- 订单，该订单表示各种类型，货架、进货，退货
    order_no bigint unsigned not null,
    order_type tinyint unsigned not null,
    user_id int unsigned not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
);

create table inventory_record_item(
    id int unsigned auto_increment primary key,
    -- 出库或入库
    repository_id int unsigned not null,
    -- 商品
    product_id int unsigned not null,
    quantity smallint unsigned not null,
    inventory_record_id int unsigned not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
)
```

## 订单核查表

```sql
create table order_review_record(
    id int unsigned auto_increment primary key,
    order_no bigint unsigned not null,
    -- 该订单核查通过
    is_passed tinyint unsigned not null default 0,
    -- 核查人
    user_id int unsigned not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
)
```