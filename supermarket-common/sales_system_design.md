## 购物车

顾客的购物车，在上架商品中选择

```sql
drop table if exists shopping_cart;
create table shopping_cart(
    id int unsigned auto_increment primary key,
    customer_id int unsigned not null,
    -- 不设计冗余，购物车需要许多信息
    shelved_product_id int unsigned not null,
    is_checked tinyint unsigned not null,
    -- 选择的数量
    quantity int unsigned not null default 0,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
);
-- 货架商品 唯一
create unique index uk_customerid_shelvedproductid on shopping_cart(customer_id, shelved_product_id);
```
