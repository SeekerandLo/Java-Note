## AOP 面向切面


### 介绍
- 程序允许期间，动态的将某段代码切入到指定方法指定位置运行，

- jar 包
    ```java
    compile group: 'org.springframework', name: 'spring-aop', version: '5.1.8.RELEASE'

    compile group: 'org.aspectj', name: 'aspectjrt', version: '1.9.4'

    compile group: 'org.aspectj', name: 'aspectjweaver', version: '1.9.4'
    ```

### 通知方法
- 前置通知

- 后置通知

- 返回通知

- 异常通知

- 环绕通知

### 举例
- 创建一个业务逻辑类

- 创建一个日志切面类

- 通知方法：
    - 前置通知 &emsp;logStart，方法运行之前运行
    - 后置通知 &emsp;logEnd，方法运行结束之后运行
    - 返回通知 &emsp;logReturn，方法正常返回之后运行
    - 异常通知 &emsp;logException，方法出现异常之后
    - 环绕通知 &emsp;动态代理，手动推进目标方法运行

- 给切面类的目标方法加注解标识何时执行

- 将切面类和目标类都加入到**容器**中

- 为切面类加上注解 `@Aspect`

- 给配置类加上 `@EnableAspectJAutoProxy` 开启注解

- 总结：
    - 将业务逻辑组件和切面组件都加入到容器中，告诉 Spring 哪个是切面类 `@Aspect`
    - 在切面类上每一个通知方法上标注注解，告诉 Spring 何时何地运行 
    - 开启基于注解的 AOP 模式 `@EnableAspectJAutoProxy`