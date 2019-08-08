## Spring 

- 注解篇
    - [注册Bean](https://github.com/SeekerandLo/Java-Note/blob/master/web%E6%A1%86%E6%9E%B6/Spring/Annotate/%E6%B3%A8%E5%86%8CBean.md)
    - [自动装配](https://github.com/SeekerandLo/Java-Note/blob/master/web%E6%A1%86%E6%9E%B6/Spring/Annotate/%E8%87%AA%E5%8A%A8%E8%A3%85%E9%85%8D.md)
    - [AOP相关注解]() 

- [IoC]()

- [AOP]()

### 概述
- Java企业级应用开发框架，为了解决企业级开发的复杂度问题(耦合度问题)。将降低耦合度的方式分为两类，IoC 和 AOP

- IoC：控制反转，对于软件来说，即某接口的具体实现类的选择控制权从调用类中移除，转交第三方决定，即由 Spring 容器借由 Bean 配置来控制
    - DI：依赖注入，让调用类对某一接口实现类的依赖关系由第三方容器注入，以移除调用类对某接口实现类的依赖

- AOP：面向切面，



### IoC

- Spring 通过一个配置文件描述 Bean 与 Bean 之间的依赖关系，利用 Java 语言的反射功能实例化 Bean 并建立 Bean 之间的依赖关系。Spring 的 IoC 容器在此基础上，还提供了 Bean 的实例缓存、生命周期管理、Bean实例代理、事件发布、资源装载等高级服务

- BeanFactory 是 Spring 最核心的接口，提供 IoC 的配置机制，ApplicationContext 建立在 BeanFactory 的基础之上，提供面向应用的功能，BeanFactory 是 Spring 框架的基础设施，ApplicationContext 是面向开发者的    

- ApplicationContext 在获取实例后可以像 BeanFactory 一样的方式获取 Bean。ApplicationContext 和 BeanFactory 有一个重大区别，ApplicationContext 在初始化容器时会一次性的初始化所有**单实例**的 Bean，而 BeanFactory 是在第一次访问某个 Bean 时才实例化目标 Bean，所以 ApplicationContext 的初始化时间要比 BeanFactory 长

### AOP

- 

### 总结

- Spring 容器在启动时会先保存注册进来的 Bean 的定义信息

- Spring 会在合适的时候创建 Bean 
    - 用到这个 Bean 的时候

    - 统一创建剩下的所有 Bean 的时候，利用 getBean 创建 Bean；创建好后保存在容器中

- 后置处理器 BeanPostProcessor

    - 每个 Bean 创建完成后都会使用各种后置处理器进行处理，来增强 Bean 的功能
        - AutowriedAnnotationBeanPostProcessor 自动注入

        - AnnotaionAwareAspectJAutoProxyCreator 做 AOP 功能

- 事件驱动模型
    - ApplicationListener 事件监听

    - ApplicationEventMulticaster 事件派发


### 开启 Spring 注解扫描
```xml
<context:component-scan base-package="包名"/>
```

### 动态代理

### Spring的优点