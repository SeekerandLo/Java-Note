## Java 注解开发

## 四大元注解

### @Target
```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Target {
    /**
     * Returns an array of the kinds of elements an annotation type
     * can be applied to.
     * @return an array of the kinds of elements an annotation type
     * can be applied to
     */
    ElementType[] value();
}
```
- 标明被标记注解可以出现在 Java 中的语法位置，注解的使用范围

- ElementType
    - TYPE ：类、接口、注解或者枚举

    - FIELD ：属性，包括枚举常量

    - METHOD ：方法

    - PARAMETER ：形参

    - CONSTRUCTOR ：构造函数

    - LOCAL_VARIABLE ：本地变量

    - ANNOTATION_TYPE ：注解

    - PACKAGE ：包

    - TYPE_PARAMETER ：类型参数

    - TYPE_USE ：类型的使用

### @Retention
```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Retention {
    /**
     * Returns the retention policy.
     * @return the retention policy
     */
    RetentionPolicy value();
}
```
- 指定注解要保留多长时间，注解的生命周期

- RetentionPolicy(enum) ：`SOURCE`、`CLASS`、`RUNTIME`
    - **SOURCE** 编译时忽略

    - **CLASS** 注释将由编译器记录在类文件中，但在运行时不需要被VM保留。这是默认行为

    - **RUNTIME** 释将被编译器记录在类文件中，在运行时保留VM，因此可以反读

### @Documented
```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Documented {
}
```
- 标明注解是否可以生成到 API 文档中
>在该注解使用后，如果导出API文档，会将该注解相关的信息可以被例如javadoc此类的工具文档化。 注意：Documented是一个标记注解，没有成员

### @Inherited
```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Inherited {
}
```
- 标明注解具有继承性，如果父类具有**该类型的注解**，那么集成了父类的子类也可以获取到该类型的注解  
ps：该类型的注解：表示注解定义中标记了 @Inherited
>假设一个注解在定义时，使用了@Inherited，然后该注解在一个类上使用，如果这个类有子类，那么通过反射我们可以从类的子类上获取到同样的注解