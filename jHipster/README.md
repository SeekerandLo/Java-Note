## 梳理任务

>
>
>战队报名 后端项目，包括：报名，查询所有队长，查询所有队员，查询所有队伍 功能

### 用户操作  

- **描述**：用户为 **比赛帮用户**，可登录用户平台进行操作，进行创建战队/查询战队/查看比赛等操作，每个用户可能是 **多个战队的队员**，也可能是 **多个战队的队长**，绑定关系应为 **每个用户在每款游戏中只能加入一个战队，类比游戏中的工会**
- **注册**
  - 输入邮箱、密码、用户名 (**用户名和邮箱均不可重复，密码后端加密**)
- **更新用户信息**
  - 更新用户个性签名，用户头像等具体信息
- **用户查询**
  - 查询自己所有战队/查询用户的战队(入参为用户id)【1】
    - 点击战队显示战队详细信息(查询战队信息，入参为战队id)【2】
  - 查询自己的信息【3】
    - 查询用户信息，获取用户详细信息，email，用户名，战绩？等信息

### 战队操作

- **前提**：所有队员已经注册成为 **比赛帮** 用户

- **报名操作**：`比赛类型未知`/`战队成员数量未知`/`战队成员关系未知`

  - **填写表单**

  1. **[必填]** 输入战队名称
  2. **[必填]** 选择游戏
  3. **[必填]** 输入队长邮箱，点击查询🕵️‍，判断该用户是否存在，获取到队长信息【5】
  4. **[必填]** 输入成员1邮箱/成员2邮箱/成员3邮箱...也点击查询，判断是否存在该用户，可一直添加用户，上限待定
  5. 战队宣言
  6. 提交：**创建成功** / **创建失败**  使用事务管理操作【4】

- **查询操作**：

  - 输入战队名称，查询战队信息（模糊搜索）【8】
    - 结果为模糊搜索的结果，点击队伍信息，获取队伍详情【2】
  - 查询所有队伍信息【6】
    - 获取数据库中全部队伍信息，结果是省略版，想获取用户信息还是点击队伍【2】
  - 查询所有队长【7】
    - 点击获取用户详细信息【3】

- **战队变更操作**：

  - 更换队长
  - 增加队员
  - 踢出队员

### 数据库

team_user : 用户表，用户id，用户email，用户名，用户密码，个性签名

team ：队伍表，队伍id，队伍名称，队伍宣言，游戏类型

team_user_info ： 用户和战队关系表，队伍id，队伍名称，用户id，用户名称，战队职位

- ```sql
  -- 查询整个队伍信息，然后对这个队伍的搜索结果进行封装，以合适的形式，返回
  select * from team_user_info where 队伍id = '**'
  ```

- ```sql
  -- 上同，模糊搜索
  select * from team_user_info where 队伍名字 like '**'
  ```

- ```sql
  -- 查询队长信息
  select * from team_user_info where 队伍id = '**' and 战队职位 = 队长
  ```

### 接口列表

【1】**查询用户队伍信息**   结果：用户参加的所有队伍，按照游戏分

【2】**获取队伍详情**   结果：队伍详细信息，可以获取队伍的所有队员

【3】**查询用户信息**   结果：用户信息，名称，邮箱等

【4】**报名**   结果：成功或失败

【5】**查询用户是否存在**，判断用户是否存在   结果：true 或者 false

【6】**查询所有队伍**   直接在数据库中查询

【7】**查询所有队长**

【8】**模糊搜索**  jpa 模糊搜索



代码还没有写完，前端和后端使用 DTO 传输数据，我又定义了一些 VO 用于后端传送给前端

***



## jHipster

&emsp;&emsp;一个代码生成工具，提供微服务应用配置服务，后端以 Spring Boot 为主，前端可以选择 Vue，Angular等

### 安装方式

1. 安装 Yeoman `npm install -g yo` 一个创建应用的工具

2. 安装 Bower `npm install -g bower` 包管理工具

3. 安装 Gulp `npm install -g gulp` 自动化构建工具

4. 安装 jHipster `npm install -g generator-jhipster` 脚手架

### 询问问题

1. Which **type** of application would you like to create?
    - Monolithic application：单体应用

    - Microservice application
    
    - Microservice gateway
    
    - JHipster UAA server

### 创建一个实体

- 可以使用 JDL 语言编写关系，然后使用命令导入进项目中

### JDL(jHipster 域语言)

- **导入 .jdl** 
    `jhipster import-jdl 目标jdl`

### 如何创建 DTO
```java
entity A {
  name String required
}
entity B
entity C


// 创建 DTO
dto A, B with mapstruct

paginate A with infinite-scroll
paginate B with pagination
paginate C with pager  // pager is only available in AngularJS

service A with serviceClass
service C with serviceImpl
```
### 导入 IDE

- ❓好多环境选择，我是除了 ecplise 全部导入了，需要理解各个环境的区别。

### jHipster 的接口层返回值处理

- ❓正在看。

## H2 数据库

&emsp;&emsp;H2 是一个嵌入式数据库

### H2数据库是以什么形式存放数据的，存放在内存中还是硬盘上

- 和创建应用时选择有关，为了想观察数据，没有选择内存模式。

- 选择存放在硬盘模式，存储数据存放在项目目录 `target/h2db/db` 下。

## JPA 操作

### 自定义语句
- 使用 @Query 时
  - 占位符问题：`:`
  - 别名问题：需要写别名，否则不能映射
  - 实体使用 `@entity` 注解，需要加 name 指定表名字，sql 语句中的表名称和这里一致 
  - 参数：参数需要使用 `@Param` 标识 
  ```java
  @Query("select t.gameType from team t where t.id = :teamId")
  Integer findGameTypeByd(@Param("teamId") Long teamId);
  ```

### 处理关系

- ❓自己在写代码时没有使用建立外键，对 JPA 的理解不够深，对处理多对多关系及一对多关系还有疑惑，正在学习。

### 流操作运用及 Optional 运用

- ❓生成的代码中大量使用了流操作和Optional ，我只会简单的应用，这里还在练习。



