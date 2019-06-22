## AOP 相关注解

1. [@PointCut]()

2. [@Before]()

3. [@After]()

4. [@AfterReturning]()

5. [@AfterThrowing]()

6. [@Around]()


### @PointCut
- 切点方法

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

- 例子
    ```java
    @Pointcut("execution(public int com.liy.spring.aop.configaop.service.Calculate.div(int, int))")
    public void pointCut() {
    }
    ```



### JoinPoint
- 获取方法的相关信息，只能写在切面方法的参数中的第一位

### @Before
- [JoinPoint](#JoinPoint)

### @After
- [JoinPoint](#JoinPoint)


### @AfterReturning
- [JoinPoint](#JoinPoint)

- returning = ""，指明接收结果的对象
    ```java
    @AfterReturning(value = "pointCut()", returning = "result")
    public void logReturn(Object result) {
        System.out.println("除法正常返回..{" + result + "}");
    }
    ```

### @AfterThrowing
- [JoinPoint](#JoinPoint)

- throwing = ""，指明抛出的异常
    ```java
    @AfterThrowing(value = "pointCut()", throwing = "exception")
    public void logException(Exception exception) {
        System.out.println("除法异常返回..{}");
    }
    ```

### @Around


### @EnableAspectJAutoProxy

- 开启基于注解的面向切面功能

- 原理介绍
    - 该注解中使用了 `@Import(AspectJAutoProxyRegistrar.class)` 引入组件，该组件实现了自定义 Bean 的功能，自定义个一个 Bean ，internalAutoProxyCreator = `AnnotationAwareAspectJAutoProxyCreator`，给容器中注册一个 `AnnotationAwareAspectJAutoProxyCreator` ，自动代理创建器

    - [AnnotationAwareAspectJAutoProxyCreator](#AnnotationAwareAspectJAutoProxyCreator)

### AnnotationAwareAspectJAutoProxyCreator