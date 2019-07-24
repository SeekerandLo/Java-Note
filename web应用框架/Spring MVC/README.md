## Spring MVC

&emsp;&emsp;Spring MVC 是 Sping 为展示层提供的一个框架，基于 MVC 的设计理念。可以通过使用注解就将一个 POJO 

成为请求处理器。

### DispatcherServlet
- DispatcherServlet 是一个前端控制器，负责接收 Http 请求和协调 Spring MVC 各组件完成请求处理工作

### SpringMVC 运行流程

- 客户端发送一个 Http 请求，服务器在接收到请求之后，如果匹配了 DispatchServlet 中的映射路径，会将请求交由 DispatchServlet 处理

- DispatchServlet 会通过 request 寻找对应的 handler
    - mappedHandler = getHandler(processedRequest);  
    这个方法最后的返回结果是一个处理器执行链，那么**过滤器也应该在这里添加**，

    - 

- HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());  
    通过HandlerAdapter对该处理器进行封装    

- Handler 完成用户的请求处理后，会返回一个 ModelAndView 对象给 DispatcherServlet

- ModelAndView的视图是逻辑视图，DispatcherServlet还要借助ViewResolver完成从逻辑视图到真实视图对象的解析工作

- 当得到真正的视图对象后，DispatcherServlet会利用视图对象对模型数据进行渲染

- 客户端得到响应，可能是一个普通的HTML页面，也可以是XML或JSON字符串，还可以是一张图片或者一个PDF文件

### DispatcherServlet 如何截获特定的 Http 请求并交由 Spring MVC 处理？


### 位于 Web 层的 Spring 容器如何与位于业务层的 Spring 容器建立关联，以 Web 层的 Bean 调用 Service 层的 Bean

### 如何初始化 Spring MVC 的各个组件，并将它们装配到 DispatcherServlet 中
