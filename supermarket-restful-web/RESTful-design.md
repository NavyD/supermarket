# RESTful Design

## User Module

### Authentication

当前仍然决定采用传统session-cookie认证

当用户成功登陆时，同时设置session_user, 与cookie_token。

_**JWT认证**_

用户的认证采用stateless JWT标准的token认证形式，不再使用session-cookie机制。

理由：

1. restful架构属于stateless，session的状态与restful架构不符
2. 使用token代替session减少应用架构复杂度，不再需要使用redis等session cache
3. 不存在跨域问题，可以放在header/url param

缺点：

使用token机制代替session并不是比session更好，相反，还存在许多问题

1. 无法进行session管理。如登录人数，强制退出登录等操作

下面是可能遇到的问题：

- 如何在刷新token时避免已存在的有效token重放。注意：如果使用https则不需要担心被恶意获取，只要确认web app不会泄露token即可

解决方法：

1. 在server中实现一个blacklist机制，保存未过期但无效的token。但是这样的方法与session机制类似，需要server保存额外的状态。

具体：

对于access_token，用户登录后，获取refresh_token。

未获取access_token访问资源时，使用refresh_token获取access_token

access_token的expiration应该比较小，如1分钟。每次请求资源时http header存在access_token，返回时直接刷新一个新的access_token，之前用过但未过期的access_token应该被放在一个blacklist中。

blacklist应该放的是JWT jti，一个类似sessionid的唯一字符串。blacklist是一个缓存，可以使用redis或local cache等实现，expiration到期时自动从blacklist中移除

下面的实现是在无法重放攻击时适用：所有被刷新的token无论是否过期都被丢弃

refresh_token:

存放user的认证信息。过期时间较长，如7天。但是在web app中可以在用户活跃时间中刷新refresh_token，如1小时

如果refresh_token过期，则需要用户重新登录

access_token:

存放用户的授权信息，时间较短，如10分钟。首次使用时使用refresh_token获取一个新的access_token，然后使用access_token访问资源

一旦检测到access_token过期，需要使用refresh_token再次获取refresh_token

注意，这一切都是在token无法被重放时可行，如果token可重放，则所有的有效被刷新的token都需要一个blacklist确定是否无效。如果是对外提供服务，即非自身把控的app，则需要防止重放攻击。所以，必须防止重放攻击，应该使用blacklist

### refresh_token

```json
    "header": {
        "alg": "HS256",
    },
    "body": {
        "iss": "app.navyd.cn",
        "jti": "unique id",
        "aud": "web app",
        "iat": "now",
        "exp": "7 day",
        // userId
        "uid“: 12323
    }
```

参考：

[实现一个靠谱的Web认证](https://www.jianshu.com/p/805dc2a0f49e)

[请教各位一个问题, 为什么 session 机制没有被 JWT 所取代?](https://neue.v2ex.com/t/381996)

### Remember Me

当用户成功登录后，根据字段`rememberMe=true`设置 记住我 功能

将remember me cookie信息保存到数据库，即使session服务器失效，仍然可以使用cookie重新登录

_**remember me cookie**_

```java
base64(username + ":" + expirationTime + ":" + md5Hex(username + ":" + expirationTime + ":" + password + ":" + key))

/*
username:          As identifiable to the UserDetailsService
password:          That matches the one in the retrieved UserDetails
expirationTime:    The date and time when the remember-me token expires, expressed in milliseconds
key:               A private key to prevent modification of the remember-me token
*/
```

使用该格式的cookie

- 验证过期时间使用expirationTime（毫秒数millisecond），而不是`cookie.maxAge`
- 当用户更改usernmae, password时cookie不再生效
- 使用`key`保证value不会被修改

#### 持久化

```sql
create table persistent_login (
    id int unsigned AUTO_INCREMENT primary key,
    username varchar(64) not null,
    hash_token varchar(64) not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp
)
```

### 限制api调用

### Forgotten Password

### 格式

当web app传递username, password, 验证码时开始验证。

#### Request For Login

```json
// method: POST
{
    "username": "user",
    "password": "password",
    // 验证码值
    "captcha": "123ab",
    "rememberMe": true,
}
```

#### Response For Login

- 错误格式类型

username的合法性由web app进行。

```json
// username不存在
// status: 403
{
    "error": "用户不存在: username",
}

// password错误
// status: 403
{
    "error": "密码不正确",
}

// 验证码错误
// status: 409 Conflict
{
    "error": "验证码错误",
}
```

- 认证成功格式

```json
// 返回当前用户的信息
// status: 200
{
    "data": {
        "username": "username",
        "iconPath": "path",
        "email": "email",
        "enabled": true,
        "phone_number": "231324",
        // 返回角色
        "role": {
            "id": 10,
            "name": "高级用户",
            "parentRole": {
                "id": 5,
                "name": "中级用户",
                "parentRole": {
                    "id": 1,
                    "name": "一般用户",
                }
            }

        },
        "gmt_create": "2019-01-15",
        "gmt_modified": "2019-01-15"
    }
}
```

### 数据表

```sql
create table user_info (
    id int unsigned auto_increment primary key,
    username varchar(20) not null,
    hash_password char(50) not null,
    icon_path varchar(255) ,
    email varchar(100) not null,
    -- 尝试登录失败次数
    failed_count tinyint unsigned not null default 0,
    -- 账户激活
    is_enabled tinyint unsigned not null default 1,
    phone_number char(15),
    role_id int unsigned not null,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp,
    unique uk_email(email(100)),
    unique uk_username (username (20))
);
```

## Role

基于Role-Based Access Control实现

### 数据表

```sql
create table role_info (
    id int unsigned auto_increment primary key,
    role_name varchar(20) not null,
    is_enabled tinyint unsigned not null default 1,
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp,
    unique uk_rolename(role_name(20))
);

create table user_role (
    user_id int unsigned not null,
    role_id int unsigned not null,
    primary key(user_id, role_id),
    gmt_create datetime not null default current_timestamp,
    gmt_modified datetime not null default current_timestamp on update current_timestamp,
);
```

## Category

### 树形结构

- [在数据库中存储一棵树，实现无限级分类](https://segmentfault.com/a/1190000014284076#articleHeader7)
- [Closure Tables for Browsing Trees in SQL](https://coderwall.com/p/lixing/closure-tables-for-browsing-trees-in-sql)