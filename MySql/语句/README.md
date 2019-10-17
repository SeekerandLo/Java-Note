## SQL语句

## DDL(数据定义语言)

### 创建数据库

- create database

```sql
-- 创建数据库
CREATE DATABASE 数据库名字
```

### 修改数据库

- alter database

```sql
ALTER DATABASE 数据库名字 { [ DEFAULT ] CHARACTER SET <字符集名> |
[ DEFAULT ] COLLATE <校对规则名>}
```

### 创建表

- create table
```sql
-- 创建表
CREATE TABLE USER(
	id INT NOT NULL PRIMARY KEY,
	names VARCHAR(10) NOT NULL
)
```

### 修改表

- alter table

### 删除表

- drop table

### 创建索引

- create index

### 删除索引

- drop index

## DML(数据操作语言)
