## AOP 面向切面

### 介绍
- 程序允许期间，动态的将某段代码切入到指定方法指定位置运行，

- jar 包
    ```java
    compile group: 'org.springframework', name: 'spring-aop', version: '5.1.8.RELEASE'

    compile group: 'org.aspectj', name: 'aspectjrt', version: '1.9.4'

    compile group: 'org.aspectj', name: 'aspectjweaver', version: '1.9.4'
    ```

## 概念介绍

### 原理
- JDK 动态代理

### 连接点

### 切入点

### 切入点表达式格式通配符和作用 execution
- execution 格式  
    execution(&emsp;`modifiers-pattern`?&emsp;`ret-type-pattern`&emsp;`declaring-type-pattern`?&emsp;`name-pattern`&emsp;(&emsp;`param-pattern`&emsp;)`throws-pattern`?&emsp;)

    modifiers-pattern：修饰符匹配  

    ret-type-pattern：返回类型匹配  
    
    declaring-type-pattern：类路径匹配  
    
    name-pattern：方法名匹配  
    
    param-pattern：参数匹配   
    
    throws-pattern：异常匹配
    
    > param-pattern  可以指定具体的参数类型，多个参数间用“,”隔开，各个参数也可以用 “\*” 来表示匹配任意类型的参数，如 (String) 表示匹配一个 String 参数的方法；(*,String) 表示匹配有两个参数的方法，第一个参数可以是任意类型，而第二个参数是 String 类型；可以用 (..) 表示零个或多个任意参数 

    `returning type pattern` `name pattern` `parameters pattern`是必须的

- 通配符 *
    
    - 返回值类型 `*`

    - 包名 `*..` 任意层级的包

    - 类名 `*`  

    - 方法名 

    - 方法形参 `*` 一个参数 ，`..` 任意参数

- 例子
    ```java
    @Pointcut("execution(public int com.liy.spring.aop.configaop.service.Calculate.div(int, int))")
    public void pointCut() {
    }
    ```

### 切入配置方式

- 局部切入点：为每个方法定义切入方法

- 切面间共享切入点：定义一个 Pointcut 方法，在每个方法中使用这个 Pointcut

- 切面内共享切入点：


### 通知

### 通知的顺序

- 执行顺序
    - 方法执行前执行   

        around-before

        before  

    - 方法执行后执行  

        around-after

        after      

        afterReturning


    - 特殊：抛出异常 

        afterThrowing

- 同一个切面的通知的执行顺序
    
    - 相同的通知类型，按指定顺序执行

    - **around 不管是在前置还是后置优先级都是最先**

- 不同切面中的通知执行顺序

    - 包裹型

- **在实际开发中，注解顺序不好控制**

### 通知获取匹配参数

- 通过加入 JoinPoint 形参，获取方法参数  

    `joinPoint.getArgs()` 获取参数数组

- 或者在通知注解中加入 argsNames
    ```java
    @Before(value = "pointCut() && args(a,b)", argNames = "joinPoint,a,b")
    public void logStart(JoinPoint joinPoint, int a, int b) {
        System.out.println("运行.." + joinPoint.getSignature().getName() + "   " + a + b);
    }
    ```



### 通知获取返回值

- @AfterReturning
    ```java
    @AfterReturning(value = "pointCut()", returning = "result")
    public void logReturn(JoinPoint joinPoint, Object result) {
        System.out.println("除法正常返回..{" + result + "}");
    }
    ```



### 引入

### 目标对象

### AOP代理

### 切面
- 也就是切面类，使用 **@Aspect** 注解标识，该类是一个注解类

### 织入

- 编译期织入
    - 执行效率高，不够灵活

- 装载时织入
    - 

- 运行时织入
    - Spring 是运行时织入

### 通知方法
- 前置通知

- 后置通知

- 返回通知

- 异常通知

- 环绕通知

### 工作流程
- 创建一个业务逻辑类(目标类)

- 创建一个日志切面类(通知类)

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

### 运行流程
- Spring 监控切入点方法对应方法是否执行

- 发现执行，立即生成切入点所在类的代理对象

- 代理对象将通知和切入点方法融合并执行（织入）