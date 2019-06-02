## Spring 

- [注解篇](https://github.com/SeekerandLo/Java-Note/blob/master/web%E6%A1%86%E6%9E%B6/Spring/%E6%B3%A8%E8%A7%A3.md)

### 概述
- Java企业级应用开发框架，为了解决企业级开发的复杂度问题(耦合度问题)。将降低耦合度的方式分为两类，IoC 和 AOP

- Ioc：控制反转，对于软件来说，即某接口的具体实现类的选择控制权从调用类中移除，转交第三方决定，即由 Spring 容器借由 Bean 配置来控制
    - DI：依赖注入，让调用类对某一接口实现类的依赖关系由第三方容器注入，以移除调用类对某接口实现类的依赖

- AOP：面向切面，


### IoC
- Spring 通过一个配置文件描述 Bean 与 Bean 之间的依赖关系，利用 Java 语言的反射功能实例化 Bean 并建立 Bean 之间的依赖关系。Spring 的 IoC 容器在此基础上，还提供了 Bean 的实例缓存、生命周期管理、Bean实例代理、事件发布、资源装载等高级服务

- BeanFactory 是 Spring 最核心的接口，提供 IoC 的配置机制，ApplicationContext 建立在 BeanFactory 的基础之上，提供面向应用的功能，BeanFactory 是 Spring 框架的基础设施，ApplicationContext 是面向开发者的    

- ApplicationContext 在获取实例后可以像 BeanFactory 一样的方式获取 Bean。ApplicationContext 和 BeanFactory 有一个重大区别，ApplicationContext 在初始化容器时会一次性的初始化所有**单实例**的 Bean，而 BeanFactory 是在第一次访问某个 Bean 时才实例化目标 Bean，所以 ApplicationContext 的初始化时间要比 BeanFactory 长

#### ApplicationContext
ApplicationContext 可以通过配置文件加载 Bean，也可以通过读取配置类的形式加载 Bean

- Spring 为基于注解类的配置提供了专门的 ApplicationContext 实现类:AnnotationConfigApplicationContext。
    ```java
    // 配置类
    @Configuration // 配置类  相当于beans.xml
    public class UserConfig {
        @Bean(name = "user") // 给容器注入一个bean，类型是返回值的类型，id是方法名
        public User user() {
            return new User("qwe", 1);
        }
    }

    // 测试类
    @Test
    public void test2() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(UserConfig.class);
        User user = (User) applicationContext.getBean("user"); 
    }
    ```
    该类可以通过一个带有 @Configuration 的类装载 Bean。

### Spring的优点