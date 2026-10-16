# Mini Store API（后端学习项目）

基于 Spring Boot 的商城后端 API（个人后端学习实践项目），目前包含购物车、商品、用户注册登录（JWT）等功能。

整体功能和接口规范参考 Spring Boot 课程案例；代码为自己边学边写，并在调试过程中自行补充了级联配置与异常处理。项目仍在持续学习和完善中，进度见下方。

## 当前进度

**已完成**

- 购物车的 6 个 RESTful 接口（创建、添加商品、查询、更新数量、移除商品、清空）
- 商品的增删改查接口
- 用户注册，以及基于 Spring Security 和 JWT 的登录认证（基础流程）
- 请求参数校验（Jakarta Validation）与全局异常处理
- 使用 DTO + MapStruct 完成 Entity 与接口数据之间的转换
- 使用 Flyway 管理数据库版本
- 数据库密码、JWT 密钥通过 `.env` 管理，不提交到仓库

**学习中 / 计划**

- 商品、用户模块抽出 Service 层
- 基于角色的权限控制（ADMIN / USER）
- 订单与结账（计划接入第三方支付）
- 单元测试和接口测试
- 使用 Spring Profiles 区分开发与生产环境配置
- 将应用与数据库部署到云端

## 技术栈

- Java 21、Maven
- Spring Boot 4.1.1
- Spring Data JPA（Hibernate）+ MySQL
- Spring Security + JWT（jjwt）
- Flyway（数据库迁移）
- MapStruct 1.6.3、Lombok
- Jakarta Validation
- springdoc-openapi
- spring-dotenv（读取 `.env`）
- Postman（接口调试）

## 项目结构

```
config                  Spring Security 配置
controllers             接口层，接收请求、返回响应
services                业务逻辑（含 JwtService、用户加载）
filters                 JWT 认证过滤器
repositories            数据访问（Spring Data JPA）
entities                实体：Cart、CartItem、Product、Category、User、Profile、Address
dtos                    接口传输对象
mappers                 MapStruct 转换接口
exceptions              自定义业务异常
validation              自定义校验注解（邮箱必须小写）
GlobalExceptionHandler  统一异常处理
```

## 接口列表

**认证与用户**

| 请求方法 | 接口路径 | 功能说明 | 是否需要登录 |
| :---: | :--- | :--- | :---: |
| POST | `/users` | 用户注册 | 否 |
| POST | `/auth/login` | 登录，返回 JWT | 否 |
| POST | `/auth/validate` | 校验 token 是否有效 | 是 |
| GET | `/users` | 查询用户列表（可按 name、email 排序） | 是 |
| GET | `/users/{id}` | 查询单个用户 | 是 |
| PUT | `/users/{id}` | 更新用户姓名、邮箱 | 是 |
| DELETE | `/users/{id}` | 删除用户 | 是 |
| POST | `/users/{id}/change-password` | 修改密码（见“已知问题”） | 是 |

**商品**

| 请求方法 | 接口路径 | 功能说明 | 是否需要登录 |
| :---: | :--- | :--- | :---: |
| GET | `/products` | 商品列表（可按 `categoryId` 筛选） | 否 |
| GET | `/products/{id}` | 商品详情 | 否 |
| POST | `/products` | 新增商品 | 是 |
| PUT | `/products/{id}` | 修改商品 | 是 |
| DELETE | `/products/{id}` | 删除商品 | 是 |

**购物车**（购物车 id 为 UUID，接口目前对外公开）

| 请求方法 | 接口路径 | 功能说明 | 成功响应码 |
| :---: | :--- | :--- | :---: |
| POST | `/carts` | 创建购物车 | 201 |
| POST | `/carts/{cartId}/items` | 添加商品（已存在则数量 +1） | 201 |
| GET | `/carts/{cartId}` | 查询购物车详情 | 200 |
| PUT | `/carts/{cartId}/items/{productId}` | 更新商品数量（1 到 100） | 200 |
| DELETE | `/carts/{cartId}/items/{productId}` | 移除购物车中的单个商品 | 204 |
| DELETE | `/carts/{cartId}/items` | 清空购物车 | 204 |

**错误响应**

| 场景          | 状态码 | 返回内容 |
|:------------| :---: | :--- |
| 购物车不存在      | 404 | `{"error": "Cart not found."}` |
| 购物车操作中商品不存在 | 400 | `{"error": "Product not found in the cart."}` |
| 请求体校验失败     | 400 | `{"字段名": "错误信息"}` |
| 登录凭证错误      | 401 | 无响应体 |

目前只有更新购物车商品数量的接口启用了请求体校验。

## 快速开始

环境要求：JDK 21、本机运行的 MySQL（默认 `localhost:3306`）。

1. 克隆项目

   ```bash
   git clone https://github.com/Gingeroy2/mini-store-api.git
   cd mini-store-api
   ```

2. 复制 `.env.example` 为 `.env`，填入自己的值：

   ```
   DB_USERNAME=root
   DB_PASSWORD=你的 MySQL 密码
   JWT_SECRET=至少 32 个字符的随机字符串
   ```

   `.env` 已被 `.gitignore` 排除，不会被提交。

3. 启动

   ```bash
   ./mvnw spring-boot:run        # Windows 使用 mvnw.cmd spring-boot:run
   ```

   首次启动会自动创建数据库 `store_api`，由 Flyway 建表，并写入示例数据（3 个分类、6 个商品，商品 id 为 1 到 6；3 个示例用户，见下方“使用示例”）。
   启动后可访问 Swagger UI 查看接口文档：`http://localhost:8080/swagger-ui.html`（调用需要登录的接口时，需在请求头中携带 token）。
   注意：直接执行 `./mvnw package` 会运行测试，测试需要数据库已启动并配置好 `.env`，否则可以加上 `-DskipTests`。

## 使用示例

**购物车**（无需登录）：先 `POST /carts` 创建购物车，拿到返回的 `id`，再调用 `POST /carts/{cartId}/items` 添加商品。

POST /carts/{cartId}/items
```json
{ "productId": 1 }
```

PUT /carts/{cartId}/items/{productId}
```json
{ "quantity": 3 }
```

**示例账号**（仅用于本地演示）：`alice@example.com`、`bob@example.com`、`carol@example.com`，密码均为 `demo123456`。

**注册与登录**

POST /users（邮箱需为小写，密码长度 6 到 25）
```json
{ "name": "Dave", "email": "dave@example.com", "password": "demo123456" }
```

POST /auth/login，返回 { "token": "..." }
```json
{ "email": "alice@example.com", "password": "demo123456" }
```

访问需要登录的接口时，在请求头中携带：`Authorization: Bearer <token>`，token 有效期 24 小时。

## 已知问题与改进计划

这是一个仍在学习过程中的项目，目前已知的不足如下，计划逐步完善：

- **分层不统一**：购物车已按 Controller / Service / Repository 分层；商品和用户模块的业务逻辑目前仍写在 Controller 中，计划抽出 Service 层。
- **修改密码接口暂不可用**：`POST /users/{id}/change-password` 还没有适配密码加密，待修复。
- **权限控制尚未完成**：目前登录用户可以操作任意用户和商品，计划按课程后续内容加入角色（ADMIN / USER）。
- **用户邮箱缺少数据库唯一约束**：目前只在代码里检查邮箱是否已存在，并发场景下可能重复，待补充迁移脚本。
- **商品详情接口**：商品不存在时目前返回 500，计划改为 404。
- **并发场景**：同一购物车同时添加同一商品时，唯一约束冲突没有专门处理。
- **测试**：尚未编写业务层单元测试，现有的上下文加载测试依赖本地数据库。
