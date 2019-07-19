### @Autowired 和 @Resource 的区别
`自动注入`
- @Autowired 默认加载是按照 Bean 类型加载的，@Resource 默认是按照 Bean 名字加载的
    ```
    getBean(*.class);

    getBean("name");
    ```
- @Autowired 是 Spring 提供的，@Resource 是 JSR250 规范中的注解

- @Autowired 可以和 @Primary 和 @Qualifier 一起使用，@Resource 不行

- @Resource 装配顺序
    
    - 在指定 name 和 type 的情况下，则从容器中寻找唯一的 Bean，找不到则异常
    
    - 如果只指定了 name，则在容器中寻找指定 name 的 Bean，找不到则异常
    
    - **如果只指定了 type，则在容器中寻找唯一的 Bean，如果找不到或找到多个则异常**
    
    - 如果都没指定，则先按定义的 Bean 的名字寻找，找不到则按类型寻找，都找不到则异常

- @Autowired 装配顺序

    - @Autowired 有一个 required 属性，如果 required 为 true，则要求必须注入一个，如果在容器中找不到，异常

    - 如果是 false，则可以是 null

    - 可以与 @Primary 和 @Qualifier 一起使用，这两个注解一般不一起用

    - @Primary 加在被注入的 Bean 上，当有相同类型的 Bean 时优先加载此类

    - @Qualifier 指明要加载的类的名字，当有多个相同类型的时候，可以使用它


### BeanFactory 和 FactoryBean 的区别
`BeanFactory`、`FactoryBean`

- BeanFactory 是一个接口，它是 Spring 创建的 Bean 的工厂，也就是 IoC 容器，Spring 使用它来管理创建的 Bean

- FactoryBean 也是一个接口，它是创建 Bean 的方式，是一个能生产或者修饰对象生成的工厂 Bean，它的实现与设计模式中的工厂模式和修饰器模式类似，当使用它


### 请说明一下Spring中BeanFactory和ApplicationContext的区别是什么？

`IoC容器`

- BeanFactory 是Spring最核心的一个接口，提供了IoC的配置机制， BeanFactory 管理不同类型的 Bean，BeanFactory 是Spring的基础设施，面向spring 本身

- ApplicationContext 也是一个接口，建立在 BeanFactory 的基础之上，又增加了更多功能，提供了国际化支持和框架事件体系，更适合创建实际应用，ApplicationContext 面向框架使用者。
 

### Spring IOC 如何实现的

`IoC`、`反射`

- 首先说明什么是 IOC ，IOC 是 Inverse of Control，即控制反转，在编程中就是某个被调用类的选择控制权从调用类中移除，交由第三方控制，而Spring 就是那个第三方。

- Spring 是通过创建了 IOC 容器，容器完成类的初始化和实例化，用户不再做创建类的工作，Spring 通过配置文件或者配置类来描述类与类之间的依赖关系，自动完成类的初始化和依赖注入工作，当需要的时候从容器中获取即可。   

- 

### Spring IoC 的原理，如果你需要实现 IoC 怎么做

`IoC`、`反射`

- IoC 是在程序运行中，动态为某个对象提供它所需要的类的技术，这是通过依赖注入实现的，比如在一个类中需要另一个类的对象，以前需要我们自己去创建这个被依赖的类的对象，有了 Spring 之后，我们就告诉Spring，我在这个类中需要那个类，交给Spring去创建对象，当系统运行时，Spring 就会将需要的对象注入到目标类中。这样就完成了各个对象之间的关系控制。

- DI 是通过反射

- 实现步骤：首先创建配置类，配置需要的 Bean，Spring 容器在初始化时会扫描这些配置，将这些 Bean 加载到容器中，当程序在运行过程中需要Bean了，会去容器中寻找

### 请简单说明一下依赖注入的方式有哪几种？以及这些方法如何使用？

- 属性注入，使用自动注入注解 @Autowired 注入

- 构造器注入，使用自动注入注解 @Autowired 注入

- set 方法注入，使用自动注入注解 @Autowired 注入


### 为什么使用 IoC
`IoC`
- 首先降低类与类之间的耦合程度，让我们从初期的用户控制转为了容器控制，不人为控制了就降低了耦合

- 解耦之后，我们可以对 Bean 做更多的事

### 介绍 Spring AOP
`AOP`
- ~~AOP 是面向切面编程，是面向对象编程的补充和扩展，它是横向的一种抽象，在方法调用链中，是纵向的，而 AOP 将纵向的方法横切一刀，在方法的周围进行操作，~~

- AOP 是面向切面编程，是面向对象编程的补充和完善，为了解决无法通过纵向继承体系减少重复性代码的问题，比如一些方法把业务方法包围在中间，这种无法通过继承来让与业务无关的代码减少，所以引入了面向切面编程，这是一种思想。将这些非业务代码抽离出来做成一个单独的模块，当程序运行时再织入进去

### Spring AOP 如何实现的

`AOP`


- 

### 你如何理解AOP中的连接点（Joinpoint）、切点（Pointcut）、增强（Advice）、引介（Introduction）、织入（Weaving）、切面（Aspect）这些概念？

`AOP`

- 连接点

- 切点

- 增强

- 引介

- 织入

- 切面


### 动态代理

### Spring 事务实现方式

### Spring 事务底层原理

### Spring MVC 实现原理

### Spring MVC 启动流程

### Spring 框架中的设计模式

### 请问Spring中Bean的作用域有哪些？

`设计模式`

- singleton 指明 Bean 以单例的形式存在，

- prototype 指以原型的形式存在，不是单例的，每次容器调用该种类型的 Bean 时，都会返回一个新的实例

- 单例模式

- 原型模式

