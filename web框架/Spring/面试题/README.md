### @Autowired 和 @Resource 的区别
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
