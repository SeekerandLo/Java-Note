## MySQL 使用相关

### 语法

JPA语法 | 例子 | 对应SQL
-- | -- | --
And | findByLastnameAndFirstname | ... where x.lastname = ?1 and x.firstname = ?2
Or | findByLastnameOrFirstname | ... where x.lastname = ?1 or x.firstname = ?2
Is | findByLastNameIs | ... where x.lastname = ?1
Equals | findByLastNameEquals | ... where x.lastname = ?1
Between | findByAgeBetween | ... where x.age between ?1 and ?2 



### 条件查询

- 当写条件查询的时候，可以自动生成 `equals` 即条件
```java
List<TeamAndUserInfo> findAllByPositionEquals(Integer positon);
```
