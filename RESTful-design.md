# RESTful Design

## User Module

### Authentication

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

注意，这一切都是在token无法被重放时可行，如果token可重放，则所有的有效被刷新的token都需要一个blacklist确定是否无效

### refresh_token

```json
    "header": {
        "alg": "HS256",
    },
    "body": {
        "iss": "app.navyd.cn",
        "aud": "web app",
        "iat": "now",
        "exp": "7 day",
        // userId
        "uid“: 12323
    }
```